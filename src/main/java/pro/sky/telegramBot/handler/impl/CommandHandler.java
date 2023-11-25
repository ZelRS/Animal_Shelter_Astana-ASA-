package pro.sky.telegramBot.handler.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.handler.Handler;
import pro.sky.telegramBot.handler.MessageHandler;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.enums.Commands.START;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class CommandHandler implements Handler {
    private final MessageHandler messageHandler;
    private final StartHandler startHandler;

    @FunctionalInterface
    interface Command {
        void run(String firstName, String lastName, Long chatId);
    }

    Map<String, Command> commandMap = new HashMap<>();

    @PostConstruct
    public void init() {
        commandMap.put(START.getName(), (firstName, lastName, chatId) -> {
            log.info("Received START command");
            startHandler.handleStartCommand(firstName, chatId);
        });
    }

    @Override
    public void handle(String command, String firstName, String lastName, Long chatId) {
        Command commandToRun = commandMap.get(command.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId);
        } else {
            log.warn("No handler found for command: {}", command);
            messageHandler.sendDefaultMessage(chatId);
        }
    }
}
