package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {

    List<Like> findAll();

    List<Like> findLikesOfFilm(Long filmId);

    Like findByUserId(Long userId);

    void saveLike(Long filmId, Long userId);

    void deleteLike(Long userId);
}
