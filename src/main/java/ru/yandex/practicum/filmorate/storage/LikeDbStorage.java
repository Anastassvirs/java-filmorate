package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Like makeLike (ResultSet rs, int rowNum) throws SQLException {
        return Like.builder()
                .film_id(rs.getLong("film_id"))
                .user_id(rs.getLong("user_id"))
                .build();
    }

    @Override
    public List<Like> findAll() {
        return jdbcTemplate.query("SELECT * FROM likes", this::makeLike);
    }

    @Override
    public List<Like> findLikesOfFilm(Long filmId) {
        String query = "SELECT * FROM likes WHERE film_id = ?;";
        return jdbcTemplate.query(query, this::makeLike, filmId);
    }

    @Override
    public Like findByUserId(Long userId) {
        String sql = "SELECT * FROM likes WHERE user_id = ?";
        try {
            log.info("Ищем лайк c user_id: {}", userId);
            return jdbcTemplate.queryForObject(sql, this::makeLike, userId);
        } catch (EmptyResultDataAccessException e) {
            log.info("Лайк с идентификатором {} не найден.", userId);
            throw new NotFoundAnythingException("Искомый лайк не существует");
        }
    }

    @Override
    public void saveLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.debug("Добавлен лайк от пользователя с id: {} к фильму с id: {}", userId, filmId);
    }

    @Override
    public void deleteLike(Long userId) {
        String sql = "DELETE FROM likes WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
