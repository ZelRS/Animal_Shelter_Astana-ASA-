package pro.sky.telegramBot.handler.usersActionHandlers.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.specificHandlers.StartHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.ActionHandler;
import pro.sky.telegramBot.sender.MessageSender;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.Command.START;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class CommandActionHandler implements ActionHandler {
    private final MessageSender messageSender;
    private final StartHandler startHandler;

    @FunctionalInterface
    interface Command {
        void run(String firstName, String lastName, Long chatId);
    }

    private final Map<String, Command> commandMap = new HashMap<>();

    // при запуске приложения происходит наполнение мапы с командами, на которые должен высылаться конкретный ответ
    @PostConstruct
    public void init() {
        commandMap.put(START.getName(), (firstName, lastName, chatId) -> {
            log.info("Received START command");
            startHandler.handleStartCommand(firstName, chatId);
        });
    }

    // метод ищет, есть ли в мапе команда по ключу.
    // Если команда есть, совершает логику, лежащую в значении по этому ключу.
    // Если такой команды в мапе нет, отправляет дефолтное сообщение
    @Override
    public void handle(String command, String firstName, String lastName, Long chatId) {
        Command commandToRun = commandMap.get(command.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId);
        } else {
            log.warn("No handler found for command: {}", command);
            // отправка дефолтного сообщения
            messageSender.sendDefaultMessage(chatId);
        }
    }
}
