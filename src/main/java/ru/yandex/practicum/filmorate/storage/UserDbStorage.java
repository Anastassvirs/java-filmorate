package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("daoUserStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final LikeStorage likeStorage;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", this::makeUser);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        LocalDate birthday =
                rs.getDate("birthday") == null ? null : rs.getDate("birthday").toLocalDate();
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(birthday)
                .build();
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try {
            log.info("Ищем пользователя c id: {}", id);
            return jdbcTemplate.queryForObject(sql, this::makeUser, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundAnythingException("Искомый пользователь не существует");
        }
    }

    @Override
    public User saveUser(User user) {
        Long id = (long) -1;
        if (validate(user)) {
            if (Objects.isNull(user.getName()) || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            String sqlQuery = "INSERT INTO users (email, login, name, birthday) " +
                    "VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getLogin());
                stmt.setString(3, user.getName());
                if (user.getBirthday() != null) {
                    stmt.setDate(4, Date.valueOf(user.getBirthday()));
                } else {
                    stmt.setDate(4, null);
                }
                return stmt;
            }, keyHolder);

            if(user.getFriends() != null) {
                for (Long friendId: user.getFriends()) {
                    String sql = "MERGE INTO friendship (user_id, friend_id) VALUES(?, ?)";
                    jdbcTemplate.update(sql, id, friendId);
                    log.debug("Друзья пользователя {} обновлены", user.getName());
                }
            }

            log.debug("Добавлен новый пользователь: {}", user);
            id = keyHolder.getKey().longValue();
        }
        return findById(id);
    }

    private static boolean validate(User user) throws ValidationException {
        if (Objects.nonNull(user.getLogin()) && user.getLogin().contains(" ")) {
            log.debug("Произошла ошибка: Поле login не может содержать пробелы");
            throw new ValidationException("Поле login не может содержать пробелы");
        } else if (Objects.nonNull(user.getBirthday()) && user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Произошла ошибка: Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }

    @Override
    public User updateUser(User user) {
        findById(user.getId());
        if(validate(user)) {
            String sqlQuery = "UPDATE users SET " +
                    "email = ?," +
                    "login = ?," +
                    "name = ?," +
                    "birthday = ? " +
                    "WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery
                    , user.getEmail()
                    , user.getLogin()
                    , user.getName()
                    , Date.valueOf(user.getBirthday())
                    , user.getId());
            if(user.getFriends() != null) {
                Set<Long> s = new LinkedHashSet<>(user.getFriends());
                String sql = "DELETE FROM friendship WHERE user_id = ?";
                jdbcTemplate.update(sql, user.getId());
                if(user.getFriends().size() != 0) {
                    for (Long friendId : s) {
                        sql = "INSERT INTO friendship (user_id, friend_id) VALUES(?, ?)";
                        jdbcTemplate.update(sql, user.getId(), friendId);
                    }
                    log.debug("Друзья пользователя {} обновлены", user.getLogin());
                }
            }
            log.debug("Обновлен пользователь: {}", user);
        }
        return user;
    }

    @Override
    public User deleteUser(User user) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getId());
        sql = "DELETE FROM friendship WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getId());
        return user;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.debug("Добавлен новая дружба между пользователями: {} и {}", userId, friendId);
        return findById(friendId);
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
        return findById(friendId);
    }

    @Override
    public List<User> findUserFriends(Long userId) {
        String sql = "SELECT *" +
              "FROM users AS u," +
              "     friendship AS f " +
              "WHERE f.friend_id = u.user_id " +
                "AND f.user_id = ?";
        return jdbcTemplate.query(sql, this::makeUser, userId);
    }

    @Override
    public List<User> findMutualFriends(Long userId, Long friendId) {
        String sql = "SELECT *" +
                "FROM users AS u," +
                "     friendship AS f, " +
                "     friendship AS f2 " +
                "WHERE f.friend_id = u.user_id AND f2.friend_id = u.user_id " +
                "AND f.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sql, this::makeUser, userId, friendId);
    }

    public void addLike(Long filmId, Long userId) {
        likeStorage.saveLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Long> findLikesOfFilm(Long filmId) {
        return likeStorage.findLikesOfFilm(filmId);
    }
}
