package pro.sky.telegramBot.handler.impl;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.handler.MessageHandler;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.utils.KeyboardCreator;
import pro.sky.telegramBot.utils.MediaMessageGenerator;

@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class MessageHandlerImpl implements MessageHandler {

    private final MediaMessageGenerator mediaMessageGenerator;
    private final MessageSender messageSender;
    private final KeyboardCreator keyboardCreator;
    private final BotConfig config;
    @Override
    public void sendWelcomeMessage(String firstName, Long chatId) {
        log.info("Sending welcome message to {}: {}", firstName, chatId);
        try {
            SendPhoto sendPhoto = mediaMessageGenerator.welcomeMessagePhotoCreate(chatId, firstName);
            sendPhoto.replyMarkup(keyboardCreator.catsAndDogsMessageKeyboard());
            messageSender.sendImageMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}: {}", firstName, chatId, e);
        }
    }

    @Override
    public void sendDefaultMessage(Long chatId) {
        log.info("Sending about message to {}", chatId);
        messageSender.sendHTMLMessage(chatId, String.format(config.getDEFAULT_MES(), chatId));
    }

    @Override
    public void sendButtonMessage(Long chatId) {

    }
}
