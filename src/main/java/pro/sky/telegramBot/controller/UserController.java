package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;

/**
 * контроллер для обработки с эндпоинтов, связанных с пользователями и информацией о них
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "API для работы с пользователями")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
    @PostMapping
    @Operation(summary = "Добавить пользователя")
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить статус пользователя")
    public void setUserState(@PathVariable("id") Long id, @RequestParam UserState state) {
        userService.setUserState(id, state);
    }
}
