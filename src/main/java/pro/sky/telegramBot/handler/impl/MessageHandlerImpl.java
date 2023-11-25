package pro.sky.telegramBot.handler.impl;

import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.handler.MessageHandler;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;

@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class MessageHandlerImpl implements MessageHandler {

//    private final MediaMessageCreator mediaMessageCreator;

    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageSender messageSender;
    private final SpecificKeyboardCreator specificKeyboardCreator;
    private final BotConfig config;
    @Override
    public void sendWelcomeMessage(String firstName, Long chatId) {
        log.info("Sending welcome message to {}: {}", firstName, chatId);
        try {
            SendPhoto sendPhoto = specificMediaMessageCreator.createWelcomeMessagePhoto(chatId, firstName);
            sendPhoto.replyMarkup(specificKeyboardCreator.petSelectionMessageKeyboard());
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
    public void sendDogsButMessage(Long chatId) {
        log.info("Sending shelter for dogs message to {}", chatId);
        try {
            SendPhoto sendPhoto = specificMediaMessageCreator.createDogSheltersListMessagePhoto(chatId);
            messageSender.sendImageMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
        }

    }

    @Override
    public void sendCatsButMessage(Long chatId) {
        log.info("Sending shelter for cats message to {}", chatId);
        try {
            SendPhoto sendPhoto = specificMediaMessageCreator.createCatSheltersListMessagePhoto(chatId);
            messageSender.sendImageMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
        }
    }

    @Override
    public void sendChooseShelterMessage(Long chatId) {

    }

    @Override
    public void sendInfoForPotentialUserMessage(Long chatId) {

    }

    @Override
    public void sendInfoForProbationUserMessage(Long chatId) {

    }

    @Override
    public void sendSorryMessage(Long chatId) {

    }

    @Override
    public void sendBlockedMessage(Long chatId) {

    }

}
