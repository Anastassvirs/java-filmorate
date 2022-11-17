package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.*;
import java.util.List;

@Slf4j
@Component
public class MPADbStorage implements MPAStorage {

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
}
