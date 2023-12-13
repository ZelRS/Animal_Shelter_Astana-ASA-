package pro.sky.telegramBot;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pro.sky.telegramBot.controller.ShelterController;
import pro.sky.telegramBot.handler.specificHandlers.impl.ShelterCommandHandler;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.service.ShelterService;

public class ClassShelterControllerTest {

    @Mock
    private ShelterService shelterService;

    @Mock
    private ShelterCommandHandler shelterCommandHandler;

    @InjectMocks
    private ShelterController shelterController;


}
