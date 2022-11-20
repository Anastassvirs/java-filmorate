package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage storage;

    public List<Genre> findAll() {
        return storage.findAll();
    }

    public List<Genre> findAllByFilmId(Long id) {
        return storage.findByFilmId(id);
    }

    public Genre findById(Long id) {
        return storage.findById(id);
    }

}
