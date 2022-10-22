package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/films")
public class FilmController {
    private HashMap<Long, Film> films;
    private Long numberOfFilms;
    private List<Film> filmList;

    public FilmController() {
        films = new HashMap();
        filmList = new ArrayList<>();
        numberOfFilms = (long) 0;
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        updateList();
        return filmList;
    }

    private void updateList() {
        filmList = new ArrayList();
        filmList.addAll(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (filmAlreadyExist(film)) {
            log.debug("Произошла ошибка: Введенный фильм уже зарегистрирован");
            throw new AlreadyExistException("Такой фильм уже зарегистрирован");
        } else if (validate(film)) {
            log.debug("Добавлен новый фильм: {}", film.toString());
            numberOfFilms++;
            film.setId(numberOfFilms);
            films.put(film.getId(), film);
        }
        return film;
    }

    private boolean filmAlreadyExist(Film film) {
        for (Film oldFilm: films.values()) {
            if (Objects.equals(oldFilm.getId(), film.getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean validate(Film film) {
        if (Objects.equals(film.getName(), null) || film.getName().equals("")) {
            log.debug("Произошла ошибка: Поле названия фильма не может быть пустым");
            throw new ValidationException("Поле названия фильма не может быть пустым");
        } else if (Objects.nonNull(film.getDescription()) && film.getDescription().length() > 200) {
            log.debug("Произошла ошибка: Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (Objects.nonNull(film.getReleaseDate())
                && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Произошла ошибка: Дата релиза должна быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        } else if (Objects.nonNull(film.getDuration()) && (film.getDuration().intValue() <= 0)) {
            log.debug("Произошла ошибка: Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        return true;
    }

    @PutMapping
    public Film updateOrCreate(@RequestBody Film film) {
        if (validate(film)) {
            if (filmAlreadyExist(film)) {
                films.put(film.getId(), film);
            } else {
                log.debug("Произошла ошибка: Введенного фильма не существует");
                throw new ValidationException("Такого фильма не существует");
            }
            log.debug("Добавлен/обновлен фильм: {}", film);
        }
        return film;
    }
}
