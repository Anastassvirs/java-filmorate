package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Service
public class FilmService {

    private FilmStorage storage;

    public FilmService(FilmStorage filmStorage) {
        this.storage = filmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        storage.findById(filmId).addLike(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        storage.findById(filmId).deleteLike(userId);
    }
}