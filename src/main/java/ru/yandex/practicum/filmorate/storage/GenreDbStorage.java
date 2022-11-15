package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        List<Genre> returnList = jdbcTemplate.query(
                "SELECT * FROM genre AS g " +
                        "RIGHT JOIN film_genre AS fg ON fg.genre_id = g.genre_id " +
                        "RIGHT JOIN film AS f ON fg.film_id = f.film_id", (rs, rowNum) -> makeGenre(rs, rowNum));
        if (returnList.size() == 1 && returnList.get(0).getId() == 0) {
            returnList = null;
        }
        return returnList;
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

    @Override
    public Genre saveGenre(Genre genre) {
        Long id = (long) -1;
        String sqlQuery = "insert into genre (name) " +
                "values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        log.debug("Добавлен новый жанр: {}", genre);
        id = keyHolder.getKey().longValue();
        return findById(id);
    }

    @Override
    public Genre updateGenre(Genre genre) {
        findById(genre.getId());
        String sqlQuery = "UPDATE genre SET " +
                "name = ? " +
                "WHERE genre_id = ?";
        jdbcTemplate.update(sqlQuery
                , genre.getName()
                , genre.getId());
        log.debug("Обновлен жанр: {}", genre);
        return genre;
    }

    @Override
    public Genre deleteGenre(Genre genre) {
        String sql = "DELETE FROM genre WHERE genre_id = ?";
        jdbcTemplate.update(sql, genre.getId());
        return genre;
    }
}
