package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
public class FilmController {

    private final HashMap<Long, Film> films = new HashMap();
    private List<Film> filmList = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        updateList();
        return filmList;
    }

    private void updateList() {
        filmList = new ArrayList();
        for (Film film: films.values()) {
            filmList.add(film);
        }
    }

    @PostMapping(value = "/film")
    public Film create(@RequestBody Film film) {
        log.debug("Добавлен новый фильм: {}", film.toString());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/film")
    public Film updateOrCreate(@RequestBody Film film) {
        if (film.getId().equals("") || film.getId() == null) {
            throw new ValidationException("Невозможно добавить пользователя без email");
        } else {
            log.debug("Добавлен/обновлен фильм: {}", film.toString());
            films.put(film.getId(), film);
            return film;
        }
    }
}
