package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;

    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }
    @GetMapping
    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return inMemoryUserStorage.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }
}
