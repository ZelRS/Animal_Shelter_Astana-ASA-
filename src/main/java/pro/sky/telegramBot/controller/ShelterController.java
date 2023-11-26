package pro.sky.telegramBot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegramBot.service.ShelterService;

// контроллер для обработки с эндпоинтов, связанных с приютами
@RestController
@RequiredArgsConstructor
@RequestMapping("/shelter")
public class ShelterController {

    private final ShelterService shelterservice;
}
