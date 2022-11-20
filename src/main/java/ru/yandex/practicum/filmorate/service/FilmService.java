package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Objects;

@Service
public class FilmService {

    private final FilmStorage storage;
    private final UserService userService;

    @Autowired
    public FilmService(UserService userService, @Qualifier("daoFilmStorage") FilmStorage storage){
        this.userService = userService;
        this.storage = storage;
    }

    public List<Film> findAll() {
        return storage.findAll();
    }

    public Film findById(Long filmId) {
        return storage.findById(filmId);
    }

    public Film createFilm(Film film) {
        return storage.saveFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    public void addLike(Long filmId, Long userId) {
        if (!Objects.isNull(filmId) && !Objects.isNull(userId) && filmId > 0 && userId > 0) {
            userService.addLike(filmId, userId);
        } else {
            throw new NotFoundAnythingException("Номер пользователя или фильма не может быть < 0 или null");
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        if (!Objects.isNull(filmId) && !Objects.isNull(userId) && filmId > 0 && userId > 0) {
            userService.deleteLike(filmId, userId);
        } else {
            throw new NotFoundAnythingException("Номер пользователя или фильма не может быть < 0 или null");
        }
    }

    public List<Film> findfirstNByLikes(Integer size) {
        return storage.findfirstNByLikes(size);
    }
}