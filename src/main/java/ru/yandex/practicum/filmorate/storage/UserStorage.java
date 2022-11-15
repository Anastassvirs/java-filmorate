package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();

    User findById(Long id);

    User saveUser(User user);

    User updateUser(User user);

    User deleteUser(User user);

    User addFriend(Long userId, Long friendId);

    User deleteFriend(Long userId, Long friendId);

    List<User> findUserFriends(Long userId);

    List<User> findMutualFriends(Long userId, Long friendId);
}
