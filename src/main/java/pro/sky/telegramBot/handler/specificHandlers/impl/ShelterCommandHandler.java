package pro.sky.telegramBot.handler.specificHandlers.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.ShelterService;
import pro.sky.telegramBot.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pro.sky.telegramBot.enums.PetType.CAT;
import static pro.sky.telegramBot.enums.PetType.DOG;

@Service
//@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class ShelterCommandHandler {
    private final MessageSender messageSender;
    private final ShelterService shelterService;
    private final UserService userService;

    @FunctionalInterface
    interface Command {
        void run(String firstName, String lastName, Long chatId);
    }

    private final Map<String, ShelterCommandHandler.Command> commandMap = new HashMap<>();

    @PostConstruct
    public void init() {
        List<Shelter> sheltersCat = shelterService.findAllShelterNamesByType(CAT);
        List<Shelter> sheltersDog = shelterService.findAllShelterNamesByType(DOG);

        int catShelterSize = sheltersCat.size();
        for (int i = 0; i < catShelterSize; i++) {
            int finalI = i;
            String refCat = "/" + (i + 1) + "_cat";
            commandMap.put(refCat, (firstName, lastName, chatId) -> {
                log.info("Received /{} CAT command", finalI);
                User user = userService.findUserByChatId(chatId);
                user.setShelter(sheltersCat.get(finalI));
                userService.update(user);
                messageSender.sendShelterFunctionalPhotoMessage(chatId);
            });
        }

        int dogShelterSize = sheltersDog.size();
        for (int i = 0; i < dogShelterSize; i++) {
            int finalI = i;
            String refDog = "/" + (i + 1) + "_dog";
            commandMap.put(refDog, (firstName, lastName, chatId) -> {
                log.info("Received /{} DOG command", finalI);
                User user = userService.findUserByChatId(chatId);
                user.setShelter(sheltersDog.get(finalI));
                userService.update(user);
                messageSender.sendShelterFunctionalPhotoMessage(chatId);
            });
        }

    }

    public void handle(String command, String firstName, String lastName, Long chatId) {
        ShelterCommandHandler.Command commandToRun = commandMap.get(command.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId);
        } else {
            log.warn("No handler found for command: {}", command);
            // отправка дефолтного сообщения
            messageSender.sendDefaultHTMLMessage(chatId);
        }
    }

    public void updateCommandMap() {
        commandMap.clear();
        init();
    }

}
