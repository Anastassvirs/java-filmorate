package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> findAll();

    Film findById(Long id);

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm(Film film);
}
