package pro.sky.telegramBot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegramBot.service.ShelterService;

@RestController
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterservice;
}
