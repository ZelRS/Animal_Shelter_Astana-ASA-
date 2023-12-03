package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegramBot.model.volunteer.Volunteer;
import pro.sky.telegramBot.service.VolunteerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/volunteer")
@Tag(name = "API для работы с волонтёром")
public class VolunteerController {

    private final VolunteerService volunteerService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить волонтёра по id")
    public ResponseEntity<Volunteer> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(volunteerService.get(id));
    }
}
