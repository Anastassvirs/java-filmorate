package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.storage = filmStorage;
    }

    public List<Film> findAll() {
        return storage.findAll();
    }

    public Film findById(Long filmId) {
        return storage.findById(filmId);
    }

    public Film createFilm(Film film) {
        return storage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    public void addLike(Long filmId, Long userId) {
        if (!Objects.isNull(filmId) && !Objects.isNull(userId) && filmId > 0 && userId > 0) {
            storage.findById(filmId).addLike(userId);
        } else {
            throw new NotFoundAnythingException("Номер пользователя или фильма не может быть < 0 или null");
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        if (!Objects.isNull(filmId) && !Objects.isNull(userId) && filmId > 0 && userId > 0) {
            storage.findById(filmId).deleteLike(userId);
        } else {
            throw new NotFoundAnythingException("Номер пользователя или фильма не может быть < 0 или null");
        }
    }

    public List<Film> findfirstNByLikes(Integer size) {
        return storage.findAll().stream()
                .sorted(this::compare)
                .limit(size)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return f1.getLikes().size() - (f0.getLikes().size());
    }
}