package pro.sky.telegramBot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegramBot.service.PetService;

@RestController
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
}
