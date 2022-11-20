package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.MPAController;
import ru.yandex.practicum.filmorate.model.MPA;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaControllerTest {

    private final MPAController mpaController;

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getMpaById() {
        MPA mpa2 = mpaController.findMPA((long) 2);
        assertThat(mpa2).hasFieldOrPropertyWithValue("id", (long) 2);
        assertThat(mpa2).hasFieldOrPropertyWithValue("name", "PG");
    }

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getAllMpa() {
        MPA mpa1 = mpaController.findMPA((long) 1);
        MPA mpa2 = mpaController.findMPA((long) 2);
        MPA mpa3 = mpaController.findMPA((long) 3);
        MPA mpa4 = mpaController.findMPA((long) 4);
        MPA mpa5 = mpaController.findMPA((long) 5);
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
