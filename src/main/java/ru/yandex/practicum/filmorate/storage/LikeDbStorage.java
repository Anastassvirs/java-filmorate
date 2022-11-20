package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    private Like makeLike (ResultSet rs, int rowNum) throws SQLException {
        return Like.builder()
                .filmId(rs.getLong("film_id"))
                .userId(rs.getLong("user_id"))
                .build();
    }

    @Override
    public List<Like> findAll() {
        return jdbcTemplate.query("SELECT * FROM likes", this::makeLike);
    }

    @Override
    public List<Long> findLikesOfFilm(Long filmId) {
        String query = "SELECT * FROM likes WHERE film_id = ?;";
        List<Like> likelist = jdbcTemplate.query(query, this::makeLike, filmId);
        List<Long> list = new ArrayList<Long>();
        for (Like like: likelist) {
            list.add(like.getUserId());
        }
        return list;
    }

    @Override
    public void saveLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.debug("Добавлен лайк от пользователя с id: {} к фильму с id: {}", userId, filmId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }
}
