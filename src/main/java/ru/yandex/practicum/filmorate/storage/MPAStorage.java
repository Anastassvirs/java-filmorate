package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPAStorage {
    List<MPA> findAll();

    MPA findById(Long id);

    MPA saveMPA(MPA mpa);

    MPA updateMPA(MPA mpa);

    MPA deleteMPA(MPA mpa);
}
