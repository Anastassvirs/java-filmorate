package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundAnythingException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private HashMap<Long, User> users;
    private Long numberOfUsers;
    private List<User> listUsers;

    public InMemoryUserStorage() {
        users = new HashMap();
        listUsers = new ArrayList<>();
        numberOfUsers = (long) 0;
    }

    @Override
    public List<User> findAll() {
        listUsers = new ArrayList();
        listUsers.addAll(users.values());
        return listUsers;
    }

    @Override
    public User saveUser(User user) {
        if (userAlreadyExist(user)) {
            log.debug("Произошла ошибка: Введенный пользователь уже зарегистрирован");
            throw new AlreadyExistException("Такой пользователь уже зарегистрирован");
        }
        if (validate(user)) {
            if (Objects.isNull(user.getName()) || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            numberOfUsers++;
            user.setId(numberOfUsers);
            log.debug("Добавлен новый пользователь: {}", user);
            users.put(numberOfUsers, user);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        if(validate(user)) {
            if (userAlreadyExist(user)) {
                users.put(user.getId(), user);
            } else {
                log.debug("Произошла ошибка: Введенного пользователя не существует");
                throw new NotFoundAnythingException("Такого пользователя не существует");
            }
            log.debug("Обновлен пользователь: {}", user);
        }
        return user;
    }

    @Override
    public User deleteUser(User user) {
        users.remove(user.getId());
        return user;
    }

    @Override
    public User findById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundAnythingException("Искомый пользователь не существует");
        }
    }

    private boolean userAlreadyExist(User user) {
        for (User oldUser: users.values()) {
            if (Objects.equals(oldUser.getId(), user.getId())) {
                return true;
            }
        }
        return false;
    }

    private static boolean validate(User user) throws ValidationException {
        if (Objects.nonNull(user.getLogin()) && user.getLogin().contains(" ")) {
            log.debug("Произошла ошибка: Поле login не может содержать пробелы");
            throw new ValidationException("Поле login не может содержать пробелы");
        } else if (Objects.nonNull(user.getBirthday()) && user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Произошла ошибка: Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }
}
