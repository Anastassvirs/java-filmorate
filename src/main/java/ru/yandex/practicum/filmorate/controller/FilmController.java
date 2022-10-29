package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable Long filmId) {
        return inMemoryFilmStorage.findById(filmId);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateOrCreate(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addFriend(@PathVariable Long filmId, Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteFriend(@PathVariable Long filmId, Long userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findFirstTenByLikes() {
        return filmService.findfirstNByLikes(10);
    }

    @GetMapping("/popular/{count}")
    public List<Film> findFirstNByLikes(@PathVariable Integer count) {
        return filmService.findfirstNByLikes(count);
    }
}
