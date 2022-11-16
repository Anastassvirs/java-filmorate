package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.*;
import java.util.List;

@Slf4j
@Component
public class MPADbStorage implements MPAStorage{

    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private MPA makeMPA(ResultSet rs, int rowNum) throws SQLException {
        return MPA.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public List<MPA> findAll() {
        return jdbcTemplate.query("SELECT * FROM mpa_values", this::makeMPA);
    }

    @Override
    public MPA findById(Long id) {
        String sql = "SELECT * FROM mpa_values WHERE mpa_id = ?";
        try {
            log.info("Ищем mpa c id: {}", id);
            return jdbcTemplate.queryForObject(sql, this::makeMPA, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("MPA с идентификатором {} не найден.", id);
            throw new NotFoundAnythingException("Искомый MPA не существует");
        }
    }

    @Override
    public MPA saveMPA(MPA mpa) {
        Long id = (long) -1;
        String sqlQuery = "insert into mpa_values (name) " +
                "values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, mpa.getName());
            return stmt;
        }, keyHolder);
        log.debug("Добавлен новый MPA: {}", mpa);
        id = keyHolder.getKey().longValue();
        return findById(id);
    }

    @Override
    public MPA updateMPA(MPA mpa) {
        findById(mpa.getId());
        String sqlQuery = "UPDATE mpa_values SET " +
                "name = ? " +
                "WHERE mpa_id = ?";
        jdbcTemplate.update(sqlQuery
                , mpa.getName()
                , mpa.getId());
        log.debug("Обновлен MPA: {}", mpa);
        return mpa;
    }

    @Override
    public MPA deleteMPA(MPA mpa) {
        String sql = "DELETE FROM mpa_values WHERE mpa_id = ?";
        jdbcTemplate.update(sql, mpa.getId());
        return mpa;
    }
}
