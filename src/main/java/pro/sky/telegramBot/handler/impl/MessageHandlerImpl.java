package pro.sky.telegramBot.handler.impl;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.handler.MessageHandler;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class MessageHandlerImpl implements MessageHandler {

//    private final MediaMessageCreator mediaMessageCreator;

    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageExecutor messageExecutor;
    private final SpecificKeyboardCreator specificKeyboardCreator;
    private final BotConfig config;
    @Override
    public void sendWelcomeMessage(String firstName, Long chatId) {
        log.info("Sending welcome message to {}: {}", firstName, chatId);
        try {
            SendPhoto sendPhoto = specificMediaMessageCreator.createWelcomeMessagePhoto(chatId, firstName);
            sendPhoto.replyMarkup(specificKeyboardCreator.petSelectionMessageKeyboard());
            messageExecutor.executeImageMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}: {}", firstName, chatId, e);
        }
    }

    @Override
    public void sendDefaultMessage(Long chatId) {
        log.info("Sending about message to {}", chatId);
        SendMessage message = new SendMessage(String.valueOf(chatId),
                String.format(config.getDEFAULT_MES())).parseMode(HTML);
        messageExecutor.executeHTMLMessage(message);
    }

    @Override
    public void sendDogsButMessage(Long chatId) {
        log.info("Sending shelter for dogs message to {}", chatId);
        try {
            SendPhoto sendPhoto = specificMediaMessageCreator.createDogSheltersListMessagePhoto(chatId);
            messageExecutor.executeImageMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
        }

    }

    @Override
    public void sendCatsButMessage(Long chatId) {
        log.info("Sending shelter for cats message to {}", chatId);
        try {
            SendPhoto sendPhoto = specificMediaMessageCreator.createCatSheltersListMessagePhoto(chatId);
            messageExecutor.executeImageMessage(sendPhoto);
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
