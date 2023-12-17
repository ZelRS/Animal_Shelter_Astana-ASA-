package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.handler.specificHandlers.impl.ShelterCommandHandler;
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
    private final ShelterCommandHandler shelterCommandHandler;

    @PostMapping
    @Operation(summary = "Создать приют",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Используется автогенерация id. Введенный id будет проигнорирован")
    )
    public ResponseEntity<Shelter> create(@RequestBody Shelter shelterRq) {
        Shelter shelter = shelterservice.create(shelterRq);
        shelterCommandHandler.updateCommandMap();
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

    @PutMapping
    @Operation(summary = "Изменить существующий приют",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Приют, подлежащий изменению определяется по полю id")
    )
    public ResponseEntity<Shelter> update(@RequestBody Shelter shelterRq) {
        Shelter shelter = shelterservice.update(shelterRq);
        shelterCommandHandler.updateCommandMap();
        return ResponseEntity.ok(shelter);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить приют по id")
    public ResponseEntity<Shelter> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(shelterservice.getById(id));
    }

    // метод для загрузки в базу схемы проезда к приюту
    @PostMapping(value = "/{id}/schema", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить схему проезда")
    public ResponseEntity<String> uploadSchema(@PathVariable("id") Long id,
                                               @RequestParam(name = "Графический файл со схемой проезда") MultipartFile multipartFile) throws Exception {
        if (!shelterservice.uploadSchema(id, multipartFile)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
