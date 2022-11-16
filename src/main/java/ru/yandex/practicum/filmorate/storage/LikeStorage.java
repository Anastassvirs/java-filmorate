package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {

    List<Like> findAll();

    List<Long> findLikesOfFilm(Long filmId);

    void saveLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);
}
