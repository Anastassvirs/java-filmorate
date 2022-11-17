package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.GenreController;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreControllerTest {

    private final GenreController genreController;

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getGenreById() {
        Genre genre2 = genreController.findGenre((long) 2);
        assertThat(genre2).hasFieldOrPropertyWithValue("id", (long) 2);
        assertThat(genre2).hasFieldOrPropertyWithValue("name", "Драма");
    }

    @Test
    @Sql(scripts = "/testsData.sql")
    public void getAllGenre() {
        Genre genre1 = genreController.findGenre((long) 1);
        Genre genre2 = genreController.findGenre((long) 2);
        Genre genre3 = genreController.findGenre((long) 3);
        Genre genre4 = genreController.findGenre((long) 4);
        Genre genre5 = genreController.findGenre((long) 5);
        Genre genre6 = genreController.findGenre((long) 6);
        assertThat(genre1).hasFieldOrPropertyWithValue("id", (long) 1);
        assertThat(genre1).hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(genre2).hasFieldOrPropertyWithValue("id", (long) 2);
        assertThat(genre2).hasFieldOrPropertyWithValue("name", "Драма");
        assertThat(genre3).hasFieldOrPropertyWithValue("id", (long) 3);
        assertThat(genre3).hasFieldOrPropertyWithValue("name", "Мультфильм");
        assertThat(genre4).hasFieldOrPropertyWithValue("id", (long) 4);
        assertThat(genre4).hasFieldOrPropertyWithValue("name", "Триллер");
        assertThat(genre5).hasFieldOrPropertyWithValue("id", (long) 5);
        assertThat(genre5).hasFieldOrPropertyWithValue("name", "Документальный");
        assertThat(genre6).hasFieldOrPropertyWithValue("id", (long) 6);
        assertThat(genre6).hasFieldOrPropertyWithValue("name", "Боевик");
    }
}
