package pro.sky.telegramBot.sender;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

// класс формирует и отправляет сообщения определенного типа в чат
@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class MessageSender {
    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageExecutor messageExecutor;
    private final SpecificKeyboardCreator specificKeyboardCreator;
    private final BotConfig config;

    // метод формирует и отправляет дефолтное сообщение в HTML формате в чат
    // дефолтное сообщение существует в качестве заглушки на случай, когда функционал не реализован
    public void sendDefaultHTMLMessage(Long chatId) {
        log.info("Sending about message to {}", chatId);
        SendMessage message = new SendMessage(String.valueOf(chatId),
                String.format(config.getMSG_DEFAULT())).parseMode(HTML);
        // выполняется отправление дефолтного сообщения в HTML формате
        messageExecutor.executeHTMLMessage(message);
    }

    // метод формирует и отправляет приветственное сообщение пользователю
    // будет формироваться сообщение с фотографией и приветственным текстом
    public void sendWelcomePhotoMessage(String firstName, Long chatId) {
        log.info("Sending welcome message to {}: {}", firstName, chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createWelcomePhotoMessage(chatId, firstName);
            // внедряется клавиатура для выбора животного
            sendPhoto.replyMarkup(specificKeyboardCreator.petSelectionMessageKeyboard());
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}: {}", firstName, chatId, e);
        }
    }

    // метод формирует и отправляет сообщение пользователю после его нажатия на кнопку "хочу собаку"
    // будет формироваться сообщение с фотографией и списком приютов для собак
    public void sendDogSheltersListPhotoMessage(Long chatId) {
        log.info("Sending shelter for dogs message to {}", chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createDogSheltersListPhotoMessage(chatId);
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
        }
    }

    // метод формирует и отправляет сообщение пользователю после его нажатия на кнопку "хочу кошку"
    // будет формироваться сообщение с фотографией и списком приютов для кошек
    public void sendCatSheltersListPhotoMessage(Long chatId) {
        log.info("Sending shelter for cats message to {}", chatId);
        try {
            SendPhoto sendPhoto = specificMediaMessageCreator.createCatSheltersListPhotoMessage(chatId);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
        }
    }

//    .........отравка сообщений пользователю на любые другие случаи........


    public void sendChooseShelterMessage(Long chatId) {

    }

    public void sendInfoForPotentialUserMessage(Long chatId) {

    }

    public void sendInfoForProbationUserMessage(Long chatId) {

    }

    public void sendSorryMessage(Long chatId) {

    }

    public void sendBlockedMessage(Long chatId) {

    }
}
