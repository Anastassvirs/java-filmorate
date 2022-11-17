package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@RestController
@RequestMapping(value = "/mpa")
public class MPAController {

    private final MPAService mpaService;

    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<MPA> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{mpaId}")
    public MPA findMPA(@PathVariable Long mpaId) {
        return mpaService.findById(mpaId);
    }

}
