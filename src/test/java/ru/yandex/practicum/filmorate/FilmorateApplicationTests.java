package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	private static UserController userController;
	private static FilmController filmController;
	private static JdbcTemplate jdbcTemplate;
	private static UserStorage userStorage;
	private static FilmStorage filmStorage;
	private static LikeStorage likeStorage;

	@BeforeAll
	static void init() {
		jdbcTemplate = new JdbcTemplate();
		userStorage = new UserDbStorage(jdbcTemplate);
		userController = new UserController(new UserService(userStorage));
		filmStorage = new FilmDbStorage(jdbcTemplate);
		likeStorage = new LikeDbStorage(jdbcTemplate);
		filmController = new FilmController(new FilmService(filmStorage, likeStorage));
	}

	@Test
	void userAdd() {
		UserStorage thatUserStorage = new UserDbStorage(jdbcTemplate);
		UserController thatUserController = new UserController(new UserService(thatUserStorage));
		User newUser = User.builder()
				.email("email@yandex.ru")
				.login("cool_user")
				.build();
		thatUserController.create(newUser);

		assertNotNull(thatUserController.findAll());
		assertEquals(1, thatUserController.findAll().size(), "Неверное количество пользователей.");
		assertEquals(newUser, thatUserController.findAll().get(0), "Пользоваетель добавлен некорректно");
	}

	@Test
	void filmAdd() {
		FilmStorage thatFilmStorage = new FilmDbStorage(jdbcTemplate);
		LikeStorage thatLikeStorage = new LikeDbStorage(jdbcTemplate);
		FilmController thatFilmController = new FilmController(new FilmService(thatFilmStorage, thatLikeStorage));
		Film newFilm = Film.builder().name("Крепкий орешек").build();
		thatFilmController.create(newFilm);

		assertNotNull(thatFilmController.findAll());
		assertEquals(1, thatFilmController.findAll().size(), "Неверное количество фильмов.");
		assertEquals(newFilm, thatFilmController.findAll().get(0), "Фильм добавлен некорректно");
	}

	@Test
	void usersAdd() {
		UserStorage thisUserstorage = new UserDbStorage(jdbcTemplate);
		userController = new UserController(new UserService(thisUserstorage));
		User newUser = User.builder()
				.email("hahahaemail@yandex.ru")
				.login("coool_user")
				.build();
		userController.create(newUser);
		User newUser2 = User.builder()
				.email("email2@yandex.ru")
				.login("not_so_cool_user")
				.build();
		userController.create(newUser2);
		User newUser3 = User.builder()
				.email("socool@yandex.ru")
				.login("super_cool_user")
				.build();
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
		FilmStorage thatFilmStorage = new FilmDbStorage(jdbcTemplate);
		LikeStorage thatLikeStorage = new LikeDbStorage(jdbcTemplate);
		filmController = new FilmController(new FilmService(thatFilmStorage, thatLikeStorage));
		Film newFilm = Film.builder().name("Зеленый фонарь").build();
		filmController.create(newFilm);
		Film newFilm2 = Film.builder().name("Зеленая книга").build();
		filmController.create(newFilm2);
		Film newFilm3 = Film.builder().name("Зеленый рыцарь").build();
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
		User newUser = User.builder().email("yeahhhhhemail@yandex.ru").login("cool_user").build();
		userController.create(newUser);

		AlreadyExistException ex = assertThrows(AlreadyExistException.class, () -> userController.create(newUser));
		assertEquals("Такой пользователь уже зарегистрирован", ex.getMessage());
	}

	@Test
	void sameFilmsAdd() {
		Film newFilm = Film.builder().name("Зеленый шершень").build();
		filmController.create(newFilm);

		AlreadyExistException ex = assertThrows(AlreadyExistException.class, () -> filmController.create(newFilm));
		assertEquals("Такой фильм уже зарегистрирован", ex.getMessage());
	}

	@Test
	void LoginContainsSpace() {
		User newUser = User.builder().email("em@yandex.r").login("cool user").build();
		ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(newUser));
		assertEquals("Поле login не может содержать пробелы", ex.getMessage());
	}

	@Test
	void BirthdayInFuture() {
		User newUser = User.builder().email("e@ya.ru").login("cool_user").build();
		newUser.setBirthday(LocalDate.of(2023, 02, 11));
		ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(newUser));
		assertEquals("Дата рождения не может быть в будущем", ex.getMessage());
	}

	@Test
	void TooLongDescription() {
		Film newFilm =Film.builder().name("Все везде и сразу").build();
		newFilm.setDescription("В жизни Эвелин царит бардак. Она никак не может составить налоговый отчёт," +
				" пожилой отец постоянно требует внимания, муж витает в облаках, а ещё и дочка привела свою" +
				" девушку знакомиться с семьёй. Во время визита в налоговую всё запутывается ещё сильнее");
		ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(newFilm));
		assertEquals("Максимальная длина описания — 200 символов", ex.getMessage());
	}

	@Test
	void TooEarlyReleaseDate() {
		Film newFilm = Film.builder().name("Шрек").build();
		newFilm.setReleaseDate(LocalDate.of(1800, 01, 1));
		ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(newFilm));
		assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", ex.getMessage());
	}

	@Test
	void TooBigDuration() {
		Film newFilm = Film.builder().name("Тор").build();
		newFilm.setDuration((long) -10);
		ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(newFilm));
		assertEquals("Продолжительность фильма должна быть положительной", ex.getMessage());
	}
}
