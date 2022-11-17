package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {

    private final UserController userController;

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getUserById() {
        User user = userController.findUser((long) 1);
        assertThat(user).hasFieldOrPropertyWithValue("id", (long) 1);
        assertThat(user).hasFieldOrPropertyWithValue("name", "user1");
        assertThat(user).hasFieldOrPropertyWithValue("login", "user1_login");
        assertThat(user).hasFieldOrPropertyWithValue("email", "user1@mail.ru");
        assertThat(user).hasFieldOrPropertyWithValue("birthday",
                LocalDate.of(2002, 12, 5));
    }

    @Test
    @Sql(scripts = "/testsData.sql")
    void userAdd() {
        User newUser = User.builder()
                .email("email@yandex.ru")
                .login("cool_user")
                .name("Джон")
                .birthday(LocalDate.of(2002, 3, 17))
                .build();
        userController.create(newUser);


        User foundndUser = userController.findUser((long) 2);
        assertThat(foundndUser).hasFieldOrPropertyWithValue("id", (long) 2);
        assertThat(foundndUser).hasFieldOrPropertyWithValue("login", "cool_user");
        assertThat(foundndUser).hasFieldOrPropertyWithValue("email", "email@yandex.ru");

        assertNotNull(userController.findAll());
    }
}
