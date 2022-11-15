package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface GenreStorage {

    List<Genre> findAll();

    List<Genre> findByFilmId(Long id);

    Genre findById(Long id);

    Genre saveGenre(Genre genre);

    Genre updateGenre(Genre genre);

    Genre deleteGenre(Genre genre);
}
