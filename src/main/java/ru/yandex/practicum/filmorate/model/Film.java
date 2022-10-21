package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Long duration;

    public Film(String name) {
        this.name = name;
    }

    public Film(String name, String description, LocalDate releaseDate, Long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
