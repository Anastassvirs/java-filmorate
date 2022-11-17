package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    private final MpaStorage storage;

    @Autowired
    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }

    public List<Mpa> findAll() {
        return storage.findAll();
    }

    public Mpa createMPA(Mpa mpa) {
        return storage.saveMPA(mpa);
    }

    public Mpa updateMPA(Mpa mpa) {
        return storage.updateMPA(mpa);
    }

    public Mpa deleteMPA(Mpa mpa) {
        return storage.deleteMPA(mpa);    }

    public Mpa findById(Long id) {
        return storage.findById(id);
    }

}
