package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.MpaController;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaControllerTest {

    private final MpaController mpaController;

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getMpaById() {
        Mpa mpa2 = mpaController.findMPA((long) 2);
        assertThat(mpa2).hasFieldOrPropertyWithValue("id", (long) 2);
        assertThat(mpa2).hasFieldOrPropertyWithValue("name", "PG");
    }

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getAllMpa() {
        Mpa mpa1 = mpaController.findMPA((long) 1);
        Mpa mpa2 = mpaController.findMPA((long) 2);
        Mpa mpa3 = mpaController.findMPA((long) 3);
        Mpa mpa4 = mpaController.findMPA((long) 4);
        Mpa mpa5 = mpaController.findMPA((long) 5);
        assertThat(mpa1).hasFieldOrPropertyWithValue("id", (long) 1);
        assertThat(mpa1).hasFieldOrPropertyWithValue("name", "G");
        assertThat(mpa2).hasFieldOrPropertyWithValue("id", (long) 2);
        assertThat(mpa2).hasFieldOrPropertyWithValue("name", "PG");
        assertThat(mpa3).hasFieldOrPropertyWithValue("id", (long) 3);
        assertThat(mpa3).hasFieldOrPropertyWithValue("name", "PG-13");
        assertThat(mpa4).hasFieldOrPropertyWithValue("id", (long) 4);
        assertThat(mpa4).hasFieldOrPropertyWithValue("name", "R");
        assertThat(mpa5).hasFieldOrPropertyWithValue("id", (long) 5);
        assertThat(mpa5).hasFieldOrPropertyWithValue("name", "NC-17");
    }
}
