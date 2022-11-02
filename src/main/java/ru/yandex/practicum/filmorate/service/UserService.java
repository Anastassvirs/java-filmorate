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
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long userId, Long friendId) {
        if (!Objects.isNull(userId) && !Objects.isNull(friendId) && userId > 0 && friendId > 0) {
            storage.findById(userId).addToFriends(friendId);
            storage.findById(friendId).addToFriends(userId);
            log.debug("Друг добавлен. Текущее количество друзей: {}", storage.findById(userId).getFriends().size());
        } else {
            throw new NotFoundAnythingException("Номер пользователя не может быть < 0 или null");
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!Objects.isNull(userId) && !Objects.isNull(friendId) && userId > 0 && friendId > 0) {
            storage.findById(userId).removeFromFriends(friendId);
            storage.findById(friendId).removeFromFriends(userId);
            log.debug("Друг удален. Текущее количество друзей: {}", storage.findById(userId).getFriends().size());
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
        log.debug("Выведен список друзей пользователя. Количество друзей: {}", storage.findById(userId).getFriends().size());
        log.debug("Друзья: {}", storage.findById(userId).getFriends());
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