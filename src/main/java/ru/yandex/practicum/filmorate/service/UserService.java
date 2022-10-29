package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long user1Id, Long user2Id) {
        storage.findById(user1Id).addToFriends(user2Id);
        storage.findById(user2Id).addToFriends(user1Id);
    }

    public void deleteFriend(Long user1Id, Long user2Id) {
        storage.findById(user1Id).removeFromFriends(user2Id);
        storage.findById(user2Id).removeFromFriends(user1Id);
    }

    public List<User> findMutualFriends(Long user1Id, Long user2Id) {
        List<User> mutualFriends = new ArrayList<>();
        Set<Long> user1Friends = storage.findById(user1Id).getFriends();
        Set<Long> user2Friends = storage.findById(user2Id).getFriends();
        user1Friends.retainAll(user2Friends);
        for (Long id : user1Friends) {
            mutualFriends.add(storage.findById(id));
        }
        return mutualFriends;
    }
}