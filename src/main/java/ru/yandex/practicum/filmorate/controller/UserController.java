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

    @GetMapping("/{user1Id}/friends/common/{user2Id}")
    public List<User> findMutualFriends(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        return userService.findMutualFriends(user1Id, user2Id);
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

    @PutMapping("/{user1Id}/friends/{user2Id}")
    public void addFriend(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        userService.addFriend(user1Id, user2Id);
    }

    @DeleteMapping("/{user1Id}/friends/{user2Id}")
    public void deleteFriend(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        userService.deleteFriend(user1Id, user2Id);
    }
}
