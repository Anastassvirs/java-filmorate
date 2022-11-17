package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> findAll();

    Mpa findById(Long id);

    Mpa saveMPA(Mpa mpa);

    Mpa updateMPA(Mpa mpa);

    Mpa deleteMPA(Mpa mpa);
}
