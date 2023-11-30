package pro.sky.telegramBot.handler.usersActionHandlers.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.specificHandlers.WelcomeMessageHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.ActionHandler;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.ShelterService;
import pro.sky.telegramBot.service.UserService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pro.sky.telegramBot.enums.Command.START;
import static pro.sky.telegramBot.enums.PetType.CAT;
import static pro.sky.telegramBot.enums.PetType.DOG;

/**
 * класс для обработки сообщения, которое должно быть выслано пользователю<br>
 * при отправке им какой-либо определенной команды
 */
@Service
@Transactional
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class CommandActionHandler implements ActionHandler {
    private final MessageSender messageSender;
    private final WelcomeMessageHandler welcomeMessageHandler;
    private final ShelterService shelterService;
    private final UserService userService;

    @FunctionalInterface
    interface Command {
        void run(String firstName, String lastName, Long chatId);
    }

    private final Map<String, Command> commandMap = new HashMap<>();

    /**
     * при запуске приложения происходит наполнение {@link #commandMap} с командами,<br>
     * при получении которых должен высылаться конкретный ответ
     */
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

        commandMap.put(START.getName(), (firstName, lastName, chatId) -> {
            log.info("Received START command");
            welcomeMessageHandler.handleStartCommand(firstName, chatId);
        });
    }

    /**
     * Метод ищет, есть ли в {@link #commandMap} кнопка по ключу.
     * Если кнопка найдена, совершается логика, лежащая по значению этого ключа.
     * Если такой команды нет, отправляется дефолтное сообщение
     */
    @Override
    public void handle(String command, String firstName, String lastName, Long chatId) {
        Command commandToRun = commandMap.get(command.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId);
        } else {
            log.warn("No handler found for command: {}", command);
            // отправка дефолтного сообщения
            messageSender.sendDefaultHTMLMessage(chatId);
        }
    }

    public Shelter create(Shelter shelter) {
        Shelter newShelter = shelterService.create(shelter);
        init();
        return newShelter;
    }

}
