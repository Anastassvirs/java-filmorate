package ru.yandex.practicum.filmorate.exception;

public class NotFoundAnythingException extends RuntimeException{
    public NotFoundAnythingException(final String message) {
        super(message);
    }
}