package ru.yandex.practicum.filmorate.exeptions;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException() {
        super();
    }

    public AlreadyExistException(final String message) {
        super(message);
    }
}
