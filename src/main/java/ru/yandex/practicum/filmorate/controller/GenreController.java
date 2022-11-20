package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping(value = "/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/filmgenres/{filmId}")
    public List<Genre> findGenresByFilm(@PathVariable Long filmID) {
        return genreService.findAllByFilmId(filmID);
    }

    @GetMapping("/{genreId}")
    public Genre findGenre(@PathVariable Long genreId) {
        return genreService.findById(genreId);
    }

}
