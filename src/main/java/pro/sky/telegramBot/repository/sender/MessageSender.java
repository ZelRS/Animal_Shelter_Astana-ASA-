package pro.sky.telegramBot.repository.sender;


import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;

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
    private Shelter selectedShelter;

    public void setSelectedShelter(Shelter selectedShelter) {
        this.selectedShelter = selectedShelter;
    }


    // метод формирует и отправляет дефолтное сообщение в HTML формате в чат
    // дефолтное сообщение существует в качестве заглушки на случай, когда функционал не реализован
    public void sendDefaultHTMLMessage(Long chatId) {
        log.info("Sending about message to {}", chatId);
        SendMessage message = new SendMessage(String.valueOf(chatId),
                String.format(config.getMSG_DEFAULT())).parseMode(HTML);
        // выполняется отправление дефолтного сообщения в HTML формате
        messageExecutor.executeHTMLMessage(message);
    }

    // метод формирует и отправляет приветственное фото-сообщение пользователю, пришедшему впервые
    public void sendFirstTimeWelcomePhotoMessage(String firstName, Long chatId) {
        log.info("Sending first time welcome message to {}: {}", firstName, chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createFirstTimeWelcomePhotoMessage(chatId, firstName);
            // внедряется клавиатура для выбора животного
            sendPhoto.replyMarkup(specificKeyboardCreator.petSelectionMessageKeyboard());
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}: {}", firstName, chatId, e);
        }
    }

    // метод формирует и отправляет приветственное фото-сообщение пользователю имеющему стутус UNTRUSTED("не надежный")
    public void sendSorryWelcomePhotoMessage(String firstName, Long chatId) {
        log.info("Sending sorry welcome message to {}: {}", firstName, chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createSorryWelcomePhotoMessage(chatId, firstName);
            // внедряется клавиатура для выбора животного
            sendPhoto.replyMarkup(specificKeyboardCreator.petSelectionMessageKeyboard());
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}: {}", firstName, chatId, e);
        }
    }

    // метод формирует и отправляет приветственное фото-сообщение пользователю имеющему стутус BLOCKED("в черном списке")
    public void sendBlockedWelcomePhotoMessage(String firstName, Long chatId) {
        log.info("Sending blocked welcome message to {}: {}", firstName, chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createBlockedWelcomePhotoMessage(chatId, firstName);
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
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createCatSheltersListPhotoMessage(chatId);
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
        }
    }

    // метод формирует и отправляет сообщение пользователю после его выбора приюта
    // будет формироваться превью приюта и кнопками выбора действия с этим приютом
    public void sendShelterFunctionalPhotoMessage(Long chatId) {
        log.info("Sending shelter functional message to {}", chatId);
        try {
            SendPhoto sendPhoto;
            if (selectedShelter.getData() != null) {
                sendPhoto = new SendPhoto(chatId, selectedShelter.getData());
            } else {
                sendPhoto = specificMediaMessageCreator.createShelterFunctionalPhotoMessage(chatId);
            }
            if (selectedShelter.getPreview() != null && !selectedShelter.getPreview().equals("")) {
                sendPhoto.caption("\"" + selectedShelter.getName() +
                        "\"\n------------\n" + selectedShelter.getPreview());
            } else {
                sendPhoto.caption(config.getMSG_SHELTER_DEFAULT_PREVIEW());
            }
            // внедряется клавиатура выбора действия пользователя c приютом
            sendPhoto.replyMarkup(specificKeyboardCreator.shelterFunctionalMessageKeyboard());
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send info message to {}", chatId, e);
        }
    }


    /////////////////////////    ЮРА ЯЦЕНКО, ТВОЙ МЕТОД ТУТ)))))))
    //    Метод используется для предоставления подробной информации о приюте
    //    Также тут пользователь может отправить свои контактные данные и создать запрос на обратный звонок
    public void sendShelterFullInfoHTMLMessage(String firstName, String lastName, Long chatId) {
        log.debug("Sending hello message to user {} with ChatID {}", firstName + " " + lastName, chatId);
        messageExecutor.executeHTMLMessage(new SendMessage(chatId, "Здравствуйте, " + firstName + " " + lastName + ".\n\n" +
                "Мы рады вас приветствовать в приюте \"" + selectedShelter.getName() + "\n\n" + "Описание приюта:\n" +
                selectedShelter.getDescription()));
    }

//    .........отправка сообщений пользователю на любые другие случаи........


    public void sendChooseShelterMessage(Long chatId) {

    }

    public void sendInfoForPotentialUserMessage(Long chatId) {

    }

    public void sendInfoForProbationUserMessage(Long chatId) {

    }
}
