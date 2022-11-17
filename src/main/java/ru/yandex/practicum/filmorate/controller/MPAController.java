package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/mpa")
public class MpaController {

    private final MpaService mpaService;

    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Mpa> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{mpaId}")
    public Mpa findMPA(@PathVariable Long mpaId) {
        return mpaService.findById(mpaId);
    }

    @PostMapping
    public Mpa create(@Valid @RequestBody Mpa mpa) {
        return mpaService.createMPA(mpa);
    }

    @PutMapping
    public Mpa update(@Valid @RequestBody Mpa mpa) {
        return mpaService.updateMPA(mpa);
    }

    @DeleteMapping
    public Mpa delete(@Valid @RequestBody Mpa mpa) {
        return mpaService.deleteMPA(mpa);
    }
}
