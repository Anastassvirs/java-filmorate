package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
public class GenreService {

    private final GenreStorage storage;

    @Autowired
    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public List<Genre> findAll() {
        return storage.findAll();
    }

    public List<Genre> findAllByFilmId(Long id) {
        return storage.findByFilmId(id);
    }

    public Genre createGenre(Genre genre) {
        return storage.saveGenre(genre);
    }

    public Genre updateGenre(Genre genre) {
        return storage.updateGenre(genre);
    }

    public Genre deleteGenre(Genre genre) {
        return storage.deleteGenre(genre);
    }

    public Genre findById(Long id) {
        return storage.findById(id);
    }

}
