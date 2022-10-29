package ru.yandex.practicum.filmorate.controller;

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

    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.userStorage = inMemoryUserStorage;
        this.userService = userService;
    }
    @GetMapping
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/{user1}/friends/{user2}")
    public void addFriend(@PathVariable Long user1Id, Long user2Id) {
        userService.addFriend(user1Id, user2Id);
    }

    @DeleteMapping("/{user1}/friends/{user2}")
    public void deleteFriend(@PathVariable Long user1Id, Long user2Id) {
        userService.deleteFriend(user1Id, user2Id);
    }

    @GetMapping("/{user1}/friends/common/{user2}")
    public List<User> findMutualFriends(@PathVariable Long user1Id, Long user2Id) {
        return userService.findMutualFriends(user1Id, user2Id);
    }
}
