package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long user1Id, Long user2Id) {
        if (!Objects.isNull(user1Id) && !Objects.isNull(user2Id) && user1Id > 0 && user2Id > 0) {
            storage.findById(user1Id).addToFriends(user2Id);
            storage.findById(user2Id).addToFriends(user1Id);
            log.debug("Друг добавлен. Текущее количество друзей: {}", storage.findById(user1Id).getFriends().size());
        } else {
            throw new NotFoundAnythingException("Номер пользователя не может быть < 0 или null");
        }
    }

    public void deleteFriend(Long user1Id, Long user2Id) {
        if (!Objects.isNull(user1Id) && !Objects.isNull(user2Id) && user1Id > 0 && user2Id > 0) {
            storage.findById(user1Id).removeFromFriends(user2Id);
            storage.findById(user2Id).removeFromFriends(user1Id);
            log.debug("Друг удален. Текущее количество друзей: {}", storage.findById(user1Id).getFriends().size());
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

    public List<User> findMutualFriends(Long user1Id, Long user2Id) {
        List<User> mutualFriends = new ArrayList<>();
        Set<Long> user1Friends = storage.findById(user1Id).getFriends();
        Set<Long> intersection = new HashSet<Long>(user1Friends);
        Set<Long> user2Friends = storage.findById(user2Id).getFriends();
        intersection.retainAll(user2Friends);
        for (Long id : intersection) {
            mutualFriends.add(storage.findById(id));
        }
        return mutualFriends;
    }
}