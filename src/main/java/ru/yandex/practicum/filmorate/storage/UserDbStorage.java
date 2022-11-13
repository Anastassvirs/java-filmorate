package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public User findById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);
        if(userRows.next()) {
            User user = new User(
                    userRows.getLong("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());

            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());

            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundAnythingException("Искомый пользователь не существует");
        }
    }

    @Override
    public User createUser(User user) {
        if (userAlreadyExist(user)) {
            log.debug("Произошла ошибка: Введенный пользователь уже зарегистрирован");
            throw new AlreadyExistException("Такой пользователь уже зарегистрирован");
        }
        if (validate(user)) {
            if (Objects.isNull(user.getName()) || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            log.debug("Добавлен новый пользователь: {}", user);
            String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?);";
            jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        }
        return user;
    }

    private boolean userAlreadyExist(User user) {
        findById(user.getId());
        return true;
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
        if(validate(user)) {
            if (userAlreadyExist(user)) {
                String sql = "INSERT INTO users (user_id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?);";
                jdbcTemplate.update(sql,
                        user.getId(),
                        user.getEmail(),
                        user.getLogin(),
                        user.getName(),
                        user.getBirthday());
            } else {
                log.debug("Произошла ошибка: Введенного пользователя не существует");
                throw new NotFoundAnythingException("Такого пользователя не существует");
            }
            log.debug("Обновлен пользователь: {}", user);
        }
        return user;
    }

    @Override
    public User deleteUser(User user) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getId());
        return user;
    }
}
