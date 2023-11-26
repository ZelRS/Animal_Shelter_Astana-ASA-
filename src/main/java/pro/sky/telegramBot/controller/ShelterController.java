package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.service.ShelterService;

// контроллер для обработки с эндпоинтов, связанных с приютами
@RestController
@RequiredArgsConstructor
@RequestMapping("/shelter")
@Tag(name = "API для работы с приютами")
public class ShelterController {
    private final ShelterService shelterservice;

    @PostMapping
    @Operation(summary = "Создать приют")
    public ResponseEntity<Shelter> create(@RequestBody Shelter shelterRq) {
        Shelter shelter = shelterservice.create(shelterRq);
        return ResponseEntity.ok(shelter);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить приют по id")
    public ResponseEntity<Shelter> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(shelterservice.getById(id));
    }
}
