package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("genre_id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT * FROM genre", (rs, rowNum) -> makeGenre(rs, rowNum));
    }

    @Override
    public List<Genre> findByFilmId(Long id) {
        String sql =   "SELECT g.genre_id, " +
                "       g.name " +
                "FROM genre AS g, " +
                "     film_genre AS fg " +
                "WHERE g.genre_id = fg.genre_id " +
                "AND fg.film_id = ?";
        return jdbcTemplate.query(sql, this::makeGenre, id);
    }

    @Override
    public Genre findById(Long id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        try {
            log.info("Ищем жанр c id: {}", id);
            return jdbcTemplate.queryForObject(sql, this::makeGenre, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new NotFoundAnythingException("Искомый жанр не существует");
        }
    }
}
