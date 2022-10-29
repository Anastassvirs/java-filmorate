package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.storage = filmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        storage.findById(filmId).addLike(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        storage.findById(filmId).deleteLike(userId);
    }

    public List<Film> findfirstNByLikes(Integer size) {
        return storage.findAll().stream()
                .sorted((p0, p1) -> compare(p0, p1))
                .limit(size)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        int result = f0.getLikes().size() - (f1.getLikes().size());
        return result;
    }
}