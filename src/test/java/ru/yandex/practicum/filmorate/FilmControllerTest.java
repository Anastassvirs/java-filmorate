package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.MPAController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {

    private final FilmController filmController;
    private final MPAController mpaController;

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getFilmById() {
        Film film = filmController.findFilm((long) 1);
        assertThat(film).hasFieldOrPropertyWithValue("id", (long) 1);
        assertThat(film).hasFieldOrPropertyWithValue("name", "Я устала");
        assertThat(film).hasFieldOrPropertyWithValue("description", "Фильм поведает историю о девушке, сдавшей спринт");
        assertThat(film).hasFieldOrPropertyWithValue("duration", (long) 100);
        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                LocalDate.of(2001, 1, 1));
        assertThat(film).hasFieldOrPropertyWithValue("rate", (long) 5);
    }

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getFilmWithMPAById() {
        Film film = filmController.findFilm((long) 1);
        assertThat(film).hasFieldOrPropertyWithValue("id", (long) 1);
        assertThat(film).hasFieldOrPropertyWithValue("name", "Я устала");
        assertThat(film).hasFieldOrPropertyWithValue("description", "Фильм поведает историю о девушке, сдавшей спринт");
        assertThat(film).hasFieldOrPropertyWithValue("duration", (long) 100);
        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                LocalDate.of(2001, 1, 1));
        assertThat(film).hasFieldOrPropertyWithValue("rate", (long) 5);
        MPA mpa = mpaController.findMPA((long) 1);
        assertThat(film).hasFieldOrPropertyWithValue("mpa", mpa);
    }
}
