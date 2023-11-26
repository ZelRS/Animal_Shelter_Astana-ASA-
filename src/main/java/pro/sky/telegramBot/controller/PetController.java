package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.service.PetService;


// контроллер для обработки с эндпоинтов, связанных с животными
@RestController
@RequiredArgsConstructor
@RequestMapping("/pet")
@Tag(name = "API для работы с живтоными")
public class PetController {
    private final PetService petService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить животное по id")
    public ResponseEntity<Pet> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(petService.getById(id));
    }
}
