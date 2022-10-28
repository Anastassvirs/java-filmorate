package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping
    public List<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateOrCreate(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }
}
