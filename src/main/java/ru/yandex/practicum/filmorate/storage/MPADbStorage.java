package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.*;
import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa makeMPA(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query("SELECT * FROM mpa_values", this::makeMPA);
    }

    @Override
    public Mpa findById(Long id) {
        String sql = "SELECT * FROM mpa_values WHERE mpa_id = ?";
        try {
            log.info("Ищем mpa c id: {}", id);
            return jdbcTemplate.queryForObject(sql, this::makeMPA, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Mpa с идентификатором {} не найден.", id);
            throw new NotFoundAnythingException("Искомый Mpa не существует");
        }
    }
}
