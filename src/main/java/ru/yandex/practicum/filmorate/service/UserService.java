package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import com.google.common.collect.Sets;
import java.util.*;

@Slf4j
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
            storage.findById(userId).addToFriends(friendId);
            storage.findById(friendId).addToFriends(userId);
            log.debug("Друг добавлен");
        } else {
            throw new NotFoundAnythingException("Номер пользователя не может быть < 0 или null");
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!Objects.isNull(userId) && !Objects.isNull(friendId) && userId > 0 && friendId > 0) {
            storage.findById(userId).removeFromFriends(friendId);
            storage.findById(friendId).removeFromFriends(userId);
            log.debug("Друг удален.");
        } else {
            throw new NotFoundAnythingException("Номер пользователя не может быть < 0 или null");
        }
    }

    public List<User> findUserFriends(Long userId) {
        List<User> listOfFriends = new ArrayList<>();
        Set<Long> setOfFriends = storage.findById(userId).getFriends();
        for (Long friendId : setOfFriends) {
            listOfFriends.add(storage.findById(friendId));
        }
        log.debug("Выведен список друзей пользователя.");
        return listOfFriends;
    }

    public List<User> findMutualFriends(Long userId, Long userSecondId) {
        List<User> mutualFriends = new ArrayList<>();
        Set<Long> user1Friends = storage.findById(userId).getFriends();
        Set<Long> user2Friends = storage.findById(userSecondId).getFriends();
        Set<Long> intersection = Sets.intersection(user1Friends, user2Friends);
        for (Long id : intersection) {
            mutualFriends.add(storage.findById(id));
        }
        return mutualFriends;
    }
}