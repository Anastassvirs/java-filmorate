package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService{

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> findAll() {
        return storage.findAll();
    }

    public User createUser(User user) {
        return storage.saveUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public User findById(Long id) {
        return storage.findById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        if (!Objects.isNull(userId) && !Objects.isNull(friendId) && userId > 0 && friendId > 0) {
            storage.addFriend(userId, friendId);
        } else {
            throw new NotFoundAnythingException("Номер пользователя не может быть < 0 или null");
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!Objects.isNull(userId) && !Objects.isNull(friendId) && userId > 0 && friendId > 0) {
            storage.deleteFriend(userId, friendId);
        } else {
            throw new NotFoundAnythingException("Номер пользователя не может быть < 0 или null");
        }
    }

    public List<User> findUserFriends(Long userId) {
        return storage.findUserFriends(userId);
    }

    public List<User> findMutualFriends(Long userId, Long userSecondId) {
        return storage.findMutualFriends(userId, userSecondId);
    }
}