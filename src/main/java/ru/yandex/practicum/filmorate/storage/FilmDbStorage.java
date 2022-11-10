package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Qualifier("dbFilmStorage")
public class FilmDbStorage implements FilmStorage{
    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film findById(Long id) {
        return null;
    }

    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film deleteFilm(Film film) {
        return null;
    }
}
