package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.PetService;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * контроллер для обработки с эндпоинтов, связанных с животными
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/pet")
@Tag(name = "API для работы с живтоными")
public class PetController {
    private final PetService petService;

    @PostMapping
    @Operation(summary = "Создать животное",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Используется автогенерация id. Введенный id будет проигнорирован")
    )
    public ResponseEntity<Pet> create(@RequestBody Pet petRq) {
        Pet pet = petService.create(petRq);
        return ResponseEntity.ok(pet);
    }

    @PostMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить фотографию животного по id")
    public ResponseEntity<String> uploadPhoto(@PathVariable("id") Long id,
                                              @RequestParam(name = "Фото животного")
                                              MultipartFile multipartFile) throws IOException {
        petService.uploadPhoto(id, multipartFile);
        return ResponseEntity.ok().build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Изменить существующее животное",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Животное, подлежащее изменению определяется по полю id")
    )
    public ResponseEntity<Pet> update(@RequestBody Pet petRq) {
        Pet pet = petService.update(petRq);
        return ResponseEntity.ok(pet);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить животное по id")
    public ResponseEntity<Pet> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(petService.getById(id));
    }
}
