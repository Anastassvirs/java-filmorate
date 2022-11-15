package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Primary
public class FilmDbStorage implements FilmStorage{

    private final JdbcTemplate jdbcTemplate;
    MPAStorage mpaStorage;
    GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
        mpaStorage = new MPADbStorage(jdbcTemplate);
        genreStorage = new GenreDbStorage(jdbcTemplate);
    }

    public List<Film> findAll() {
        return jdbcTemplate.query("SELECT * FROM film", (rs, rowNum) -> makeFilm(rs, rowNum));
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .rate(rs.getInt("rate"))
                .mpa(mpaStorage.findById(rs.getLong("mpa_rate_id")))
                .genres(genreStorage.findByFilmId(rs.getLong("film_id")))
                .build();
    }

    @Override
    public Film findById(Long id) {
        String sql = "SELECT * FROM film WHERE film_id = ?";
        try {
            log.info("Ищем фильм c id: {}", id);
            return jdbcTemplate.queryForObject(sql, this::makeFilm, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundAnythingException("Искомый фильм не существует");
        }
    }

    @Override
    public Film saveFilm(Film film) {
        Long id = (long) -1;
        if (validate(film)) {
            String sqlQuery = "insert into film (name, description, release_date, duration, rate, mpa_rate_id) " +
                    "values (?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setLong(4, film.getDuration());
                stmt.setLong(5, film.getRate());
                stmt.setLong(6, film.getMpa().getId());
                return stmt;
            }, keyHolder);
            log.debug("Добавлен " +
                    "новый фильм: {}", film);
            id = keyHolder.getKey().longValue();

            if(film.getGenres() != null) {
                for (Genre genre: film.getGenres()) {
                    String sql = "INSERT INTO film_genre VALUES(?, ?)";
                    jdbcTemplate.update(sql, id, genre.getId());
                    log.debug("Жанры фильма {} обновлены", film.getName());
                }
            }
        }
        Film filmre = findById(id);
        System.out.println("filmre = " + filmre);
        return filmre;
    }

    private boolean validate(Film film) {
        if (Objects.nonNull(film.getDescription()) && film.getDescription().length() > 200) {
            log.debug("Произошла ошибка: Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (Objects.nonNull(film.getReleaseDate())
                && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Произошла ошибка: Дата релиза должна быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        } else if (Objects.nonNull(film.getDuration()) && (film.getDuration().intValue() <= 0)) {
            log.debug("Произошла ошибка: Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        return true;
    }

    @Override
    public Film updateFilm(Film film) {
        findById(film.getId());
        if(validate(film)) {
            String sqlQuery = "UPDATE film SET " +
                    "name = ?," +
                    "description = ?," +
                    "release_date = ?," +
                    "duration = ?," +
                    "rate = ?," +
                    "mpa_rate_id = ? " +
                    "WHERE film_id = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , Date.valueOf(film.getReleaseDate())
                    , film.getDuration()
                    , film.getRate()
                    , film.getMpa().getId()
                    , film.getId());
            if(film.getGenres() != null) {
                for (Genre genre: film.getGenres()) {
                    String sql = "INSERT INTO film_genre VALUES(?, ?)";
                    jdbcTemplate.update(sql, film.getId(), genre.getId());
                    log.debug("Жанры фильма {} обновлены", film.getName());
                }
            }
            log.debug("Обновлен фильм: {}", film);
        }
        return findById(film.getId());
    }

    @Override
    public Film deleteFilm(Film film) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
        return film;
    }
}
