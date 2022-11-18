package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Service
public class MPAService {

    private final MPAStorage storage;

    @Autowired
    public MPAService(MPAStorage storage) {
        this.storage = storage;
    }

    public List<MPA> findAll() {
        return storage.findAll();
    }

    public MPA findById(Long id) {
        return storage.findById(id);
    }

}
