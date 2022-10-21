package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(String email, String login) {
        this.email = email;
        this.login = login;
    }
}
