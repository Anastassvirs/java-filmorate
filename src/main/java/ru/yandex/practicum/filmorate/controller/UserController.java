package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.userStorage = inMemoryUserStorage;
        this.userService = userService;
    }
    @GetMapping
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable Long userId) {
        return userStorage.findById(userId);
    }

    @GetMapping("/{userId}/friends/common/{userSecondId}")
    public List<User> findMutualFriends(@PathVariable Long userId, @PathVariable Long userSecondId) {
        return userService.findMutualFriends(userId, userSecondId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> findUserFriends(@PathVariable Long userId) {
        return userService.findUserFriends(userId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
    }
}
