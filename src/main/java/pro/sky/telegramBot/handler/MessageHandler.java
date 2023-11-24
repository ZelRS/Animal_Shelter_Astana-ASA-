package pro.sky.telegramBot.handler;

import java.io.IOException;

public interface MessageHandler {
    void sendWelcomeMessage(String firstName, Long chatId) throws IOException;

    void sendDefaultMessage(Long chatId);

    void sendDogsButMessage(Long chatId);

    void sendChooseShelterMessage(Long chatId);

    void sendInfoForPotentialUserMessage(Long chatId);

    void sendInfoForProbationUserMessage(Long chatId);

    void sendSorryMessage(Long chatId);

    void sendBlockedMessage(Long chatId);

    void sendCatsButMessage(Long chatId);
}
