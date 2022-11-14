package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAll();

    Film findById(Long id);

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(Film film);
}
