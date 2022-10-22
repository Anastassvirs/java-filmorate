package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private HashMap<Long, User> users;
    private Long numberOfUsers;
    private List<User> listUsers;

    public UserController() {
        users = new HashMap();
        listUsers = new ArrayList<>();
        numberOfUsers = (long) 0;
    }

    @GetMapping
    public List<User> findAll() {
        updateList();
        return listUsers;
    }

    private void updateList() {
        listUsers = new ArrayList();
        listUsers.addAll(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (userAlreadyExist(user)) {
            log.debug("Произошла ошибка: Введенный пользователь уже зарегистрирован");
            throw new AlreadyExistException("Такой пользователь уже зарегистрирован");
        } else {
            if (validate(user)) {
                if (Objects.isNull(user.getName()) || user.getName().equals("")) {
                    user.setName(user.getLogin());
                }
                log.debug("Добавлен новый пользователь: {}", user);
                numberOfUsers++;
                user.setId(numberOfUsers);
                users.put(numberOfUsers, user);
            }
            return user;
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

    private static boolean validate(User user) throws ValidationException{
        if (Objects.nonNull(user.getLogin()) && user.getLogin().contains(" ")) {
            log.debug("Произошла ошибка: Поле login не может содержать пробелы");
            throw new ValidationException("Поле login не может содержать пробелы");
        } else if (Objects.nonNull(user.getBirthday()) && user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Произошла ошибка: Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }

    @PutMapping
    public User updateOrCreate(@Valid @RequestBody User user) {
        if(validate(user)) {
            if (userAlreadyExist(user)) {
                users.put(user.getId(), user);
            } else {
                log.debug("Произошла ошибка: Введенного пользователя не существует");
                throw new ValidationException("Такого пользователя не существует");
            }
            log.debug("Обновлен/добавлен пользователь: {}", user);
        }
        return user;
    }
}
