package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	private static UserController userController;
	private static FilmController filmController;

	@BeforeAll
	static void init() {
		InMemoryUserStorage storage = new InMemoryUserStorage();
		userController = new UserController(storage, new UserService(storage));
		filmController = new FilmController(new InMemoryFilmStorage());
	}

	@Test
	void userAdd() {
		InMemoryUserStorage storage = new InMemoryUserStorage();
		userController = new UserController(storage, new UserService(storage));
		User newUser = new User("email@yandex.ru", "cool_user");
		userController.create(newUser);

		assertNotNull(userController.findAll());
		assertEquals(1, userController.findAll().size(), "Неверное количество пользователей.");
		assertEquals(newUser, userController.findAll().get(0), "Пользоваетель добавлен некорректно");
	}

	@Test
	void filmAdd() {
		filmController = new FilmController(new InMemoryFilmStorage());
		Film newFilm = new Film("Крепкий орешек");
		filmController.create(newFilm);

		assertNotNull(filmController.findAll());
		assertEquals(1, filmController.findAll().size(), "Неверное количество фильмов.");
		assertEquals(newFilm, filmController.findAll().get(0), "Фильм добавлен некорректно");
	}

	@Test
	void usersAdd() {
		InMemoryUserStorage storage = new InMemoryUserStorage();
		userController = new UserController(storage, new UserService(storage));
		User newUser = new User("hahahaemail@yandex.ru", "cool_user");
		userController.create(newUser);
		User newUser2 = new User("email2@yandex.ru", "not_so_cool_user");
		userController.create(newUser2);
		User newUser3 = new User("socool@yandex.ru", "super_cool_user");
		userController.create(newUser3);

		User[] usersRef = new User[] {newUser, newUser2, newUser3};
		List<User> userList = userController.findAll();
		User[] users = userList.toArray(User[]::new);

		assertNotNull(userController.findAll());
		assertEquals(3, userController.findAll().size(), "Неверное количество пользователей.");
		assertArrayEquals(usersRef, users, "Пользоваетели добавлены некорректно");
	}

	@Test
	void filmsAdd() {
		filmController = new FilmController(new InMemoryFilmStorage());
		Film newFilm = new Film("Зеленый фонарь");
		filmController.create(newFilm);
		Film newFilm2 = new Film("Зеленая книга");
		filmController.create(newFilm2);
		Film newFilm3 = new Film("Зеленый рыцарь");
		filmController.create(newFilm3);

		Film[] filmsRef = new Film[] {newFilm, newFilm2, newFilm3};
		List<Film> filmList = filmController.findAll();
		Film[] films = filmList.toArray(Film[]::new);

		assertNotNull(filmController.findAll());
		assertEquals(3, filmController.findAll().size(), "Неверное количество фильмов.");
		assertArrayEquals(filmsRef, films, "Фильмы добавлены некорректно");
	}

	@Test
	void sameUsersAdd() {
		User newUser = new User("yeahhhhhemail@yandex.ru", "cool_user");
		userController.create(newUser);

		AlreadyExistException ex = assertThrows(AlreadyExistException.class, () -> userController.create(newUser));
		assertEquals("Такой пользователь уже зарегистрирован", ex.getMessage());
	}

	@Test
	void sameFilmsAdd() {
		Film newFilm = new Film("Зеленый шершень");
		filmController.create(newFilm);

		AlreadyExistException ex = assertThrows(AlreadyExistException.class, () -> filmController.create(newFilm));
		assertEquals("Такой фильм уже зарегистрирован", ex.getMessage());
	}

	@Test
	void LoginContainsSpace() {
		User newUser = new User("em@yandex.r", "cool user");

		ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(newUser));
		assertEquals("Поле login не может содержать пробелы", ex.getMessage());
	}

	@Test
	void BirthdayInFuture() {
		User newUser = new User("e@ya.ru", "cool_user");
		newUser.setBirthday(LocalDate.of(2023, 02, 11));
		ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(newUser));
		assertEquals("Дата рождения не может быть в будущем", ex.getMessage());
	}

	@Test
	void TooLongDescription() {
		Film newFilm = new Film("Все везде и сразу");
		newFilm.setDescription("В жизни Эвелин царит бардак. Она никак не может составить налоговый отчёт," +
				" пожилой отец постоянно требует внимания, муж витает в облаках, а ещё и дочка привела свою" +
				" девушку знакомиться с семьёй. Во время визита в налоговую всё запутывается ещё сильнее");
		ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(newFilm));
		assertEquals("Максимальная длина описания — 200 символов", ex.getMessage());
	}

	@Test
	void TooEarlyReleaseDate() {
		Film newFilm = new Film("Шрек");
		newFilm.setReleaseDate(LocalDate.of(1800, 01, 1));
		ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(newFilm));
		assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", ex.getMessage());
	}

	@Test
	void TooBigDuration() {
		Film newFilm = new Film("Тор");
		newFilm.setDuration((long) -10);
		ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(newFilm));
		assertEquals("Продолжительность фильма должна быть положительной", ex.getMessage());
	}
}
