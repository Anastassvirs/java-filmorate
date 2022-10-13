package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exeptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
public class UserController {

    private final HashMap<String, User> users = new HashMap();
    private List<User> listUsers = new ArrayList<>();

    @GetMapping("/users")
    public List<User> findAll() {
        updateList();
        return listUsers;
    }

    private void updateList() {
        listUsers = new ArrayList();
        for (User user: users.values()) {
            listUsers.add(user);
        }
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        if (userAlreadyExist(user)) {
            throw new UserAlreadyExistException("Такой пользователь уже зарегистрирован");
        } else if (user.getEmail().equals("") || user.getEmail() == null) {
            throw new ValidationException("Поле email не может быть пустым");
        } else if (!user.getEmail().contains("@")) {
            throw new ValidationException("Поле email должно содержать символ @");
        } else if (user.getLogin().equals("") || user.getLogin() == null) {
            throw new ValidationException("Поле login не может быть пустым");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("Поле login не может содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else {
            if (user.getName().equals("") || user.getName() == null) {
                user.setName(user.getLogin());
            }
            log.debug("Добавлен новый пользователь: {}", user.toString());
            users.put(user.getEmail(), user);
            return user;
        }
    }

    private boolean userAlreadyExist(User user) {
        boolean userExist = false;
        for (User oldUser: users.values()) {
            if (oldUser.equals(user)) {
                userExist = true;
                return userExist;
            }
        }
        return userExist;
    }

    @PutMapping(value = "/users")
    public User updateOrCreate(@RequestBody User user) {
        if (user.getEmail().equals("") || user.getEmail() == null) {
            return user;
        } else {
            users.put(user.getEmail(), user);
            return user;
        }
    }
}
