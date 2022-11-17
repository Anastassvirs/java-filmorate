package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.GenreController;
import ru.yandex.practicum.filmorate.controller.MpaController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateApplicationTests {

	private final UserController userController;
	private final FilmController filmController;
	private final MpaController mpaController;
	private final GenreController genreController;

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
		Mpa mpa = mpaController.findMPA((long) 1);
		assertThat(film).hasFieldOrPropertyWithValue("mpa", mpa);
	}

	@Test
	@Sql(scripts = "/testsData.sql")
	public void getMPAById() {
		Mpa mpa2 = mpaController.findMPA((long) 2);
		Mpa mpa3 = mpaController.findMPA((long) 3);
		Mpa mpa6 = mpaController.findMPA((long) 5);
		assertThat(mpa2).hasFieldOrPropertyWithValue("id", (long) 2);
		assertThat(mpa2).hasFieldOrPropertyWithValue("name", "PG");
		assertThat(mpa3).hasFieldOrPropertyWithValue("id", (long) 3);
		assertThat(mpa3).hasFieldOrPropertyWithValue("name", "PG-13");
		assertThat(mpa6).hasFieldOrPropertyWithValue("id", (long) 5);
		assertThat(mpa6).hasFieldOrPropertyWithValue("name", "NC-17");
	}

	@Test
	@Sql(scripts = "/testsData.sql")
	public void getGenreById() {
		Genre genre2 = genreController.findGenre((long) 2);
		Genre genre4 = genreController.findGenre((long) 4);
		Genre genre6 = genreController.findGenre((long) 6);
		assertThat(genre2).hasFieldOrPropertyWithValue("id", (long) 2);
		assertThat(genre2).hasFieldOrPropertyWithValue("name", "Драма");
		assertThat(genre4).hasFieldOrPropertyWithValue("id", (long) 4);
		assertThat(genre4).hasFieldOrPropertyWithValue("name", "Триллер");
		assertThat(genre6).hasFieldOrPropertyWithValue("id", (long) 6);
		assertThat(genre6).hasFieldOrPropertyWithValue("name", "Боевик");
	}
}
