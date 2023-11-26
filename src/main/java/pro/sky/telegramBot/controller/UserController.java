package pro.sky.telegramBot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegramBot.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
}
