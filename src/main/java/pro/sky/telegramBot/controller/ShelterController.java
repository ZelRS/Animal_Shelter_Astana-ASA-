package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.handler.usersActionHandlers.impl.CommandActionHandler;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.service.ShelterService;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * контроллер для обработки с эндпоинтов, связанных с приютами
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shelter")
@Tag(name = "API для работы с приютами")
public class ShelterController {
    private final ShelterService shelterservice;
    private final CommandActionHandler commandActionHandler;

    @PostMapping
    @Operation(summary = "Создать приют")
    public ResponseEntity<Shelter> create(@RequestBody Shelter shelterRq) {
        Shelter shelter = commandActionHandler.create(shelterRq);
        return ResponseEntity.ok(shelter);
    }

    @PostMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить фотографию приюта по id")
    public ResponseEntity<String> uploadPhoto(@PathVariable("id") Long id,
                                              @RequestParam(name = "Фото приюта")
                                              MultipartFile multipartFile) throws IOException {
        shelterservice.uploadPhoto(id, multipartFile);
        return ResponseEntity.ok().build();
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Изменить существующий приют")
    public ResponseEntity<Shelter> update(@RequestBody Shelter shelterRq) {
        Shelter shelter = shelterservice.update(shelterRq);
        return ResponseEntity.ok(shelter);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить приют по id")
    public ResponseEntity<Shelter> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(shelterservice.getById(id));
    }
}
