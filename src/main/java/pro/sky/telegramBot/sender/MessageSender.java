package pro.sky.telegramBot.sender;


import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;

/**
 * методы класса группируют компоненты и формируют сообщения ответа,<br>
 * а затем вызывают {@link #messageExecutor}, который выполняет отправку<br>
 * этого сообщения
 */
@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class MessageSender {
    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageExecutor messageExecutor;
    private final SpecificKeyboardCreator specificKeyboardCreator;
    private final BotConfig config;
    private final UserService userService;

    /**
     * метод формирует и отправляет дефолтное сообщение в HTML формате
     */
    // дефолтное сообщение существует в качестве заглушки на случай, когда функционал не реализован
    public void sendDefaultHTMLMessage(Long chatId) {
        log.info("Sending about message to {}", chatId);
        SendMessage message = new SendMessage(String.valueOf(chatId),
                String.format(config.getMSG_DEFAULT())).parseMode(HTML);
        // выполняется отправление дефолтного сообщения в HTML формате
        messageExecutor.executeHTMLMessage(message);
    }

    /**
     * метод формирует и отправляет приветственное фото-сообщение пользователю, пришедшему впервые
     */
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

    /**
     * метод формирует и отправляет приветственное фото-сообщение пользователю,<br>
     * имеющему статус UNTRUSTED("не надежный")
     */
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

    /**
     * метод формирует и отправляет приветственное фото-сообщение пользователю,<br>
     * имеющему статус BLOCKED("в черном списке")
     */
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

    /**
     * метод формирует и отправляет сообщение пользователю после его нажатия на кнопку "хочу собаку"
     */
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

    /**
     * метод формирует и отправляет сообщение пользователю после его нажатия на кнопку "хочу кошку"
     */
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

    /**
     * метод формирует и отправляет превью сообщение пользователю после его выбора конкретного приюта
     */
    // будет формироваться превью приюта и кнопками выбора действия с этим приютом
    public void sendShelterFunctionalPhotoMessage(Long chatId) {
        log.info("Sending shelter functional message to {}", chatId);
        try {
            SendPhoto sendPhoto;
            User user = userService.findUserByChatId(chatId);
            // если у приюта нет фото, будет высылаться дефолное фото
            if (user.getShelter().getData() != null) {
                sendPhoto = new SendPhoto(chatId, user.getShelter().getData());
            } else {
                sendPhoto = specificMediaMessageCreator.createShelterFunctionalPhotoMessage(chatId);
            }
            // если у приюта нет превью, будет высылать дефолтное превью
            if (user.getShelter().getPreview() != null && !user.getShelter().getPreview().equals("")) {
                sendPhoto.caption("\"" + user.getShelter().getName() +
                        "\"\n------------\n" + user.getShelter().getPreview());
            } else {
                sendPhoto.caption(config.getMSG_SHELTER_DEFAULT_PREVIEW());
            }
            // внедряется клавиатура выбора действия пользователя c приютом
            sendPhoto.replyMarkup(specificKeyboardCreator.shelterFunctionalMessageKeyboard());
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send shelter functional message to {}", chatId, e);
        }
    }


    /////////////////////////    ЮРА ЯЦЕНКО, ТВОЙ МЕТОД ТУТ)))))))

    /**
     * метод формирует и отправляет сообщение пользователю<br>
     * для предоставления подробной информации о приюте
     */
    public void sendShelterFullInfoHTMLMessage(String firstName, String lastName, Long chatId) {
        log.debug("Sending hello message to user {} with ChatID {}", firstName + " " + lastName, chatId);
        User user = userService.findUserByChatId(chatId);
        messageExecutor.executeHTMLMessage(new SendMessage(chatId, "Здравствуйте, " + firstName + " " + lastName + ".\n\n" +
                "Мы рады вас приветствовать в приюте \"" + user.getShelter().getName() + "\n\n" + "Описание приюта:\n" +
                user.getShelter().getDescription()));
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "взять животное"
     */
    public void sendTakingPetPhotoMessage(Long chatId, String firstName) {
        log.info("Sending \"Taking Pet\" message to {}", chatId);
        try {
            SendPhoto sendPhoto;
            User user = userService.findUserByChatId(chatId);
            // происходит проверка статуса пользователя на UNTRUSTED и BLOCKED
            if (user.getState().equals(UserState.UNTRUSTED)) {
                sendPhoto = specificMediaMessageCreator.createSorryWelcomePhotoMessage(chatId, firstName);
            } else if (user.getState().equals(UserState.BLOCKED)) {
                sendPhoto = specificMediaMessageCreator.createBlockedWelcomePhotoMessage(chatId, firstName);
            } else {
                sendPhoto = specificMediaMessageCreator.createTakingPetPhotoMessage(chatId, firstName);
                sendPhoto.replyMarkup(specificKeyboardCreator.takingPetMessageKeyboard());
            }
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send \"Taking Pet\" message to {}", chatId, e);
        }
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "Рекомендации и советы"
     */
    public void sendCarePetRecMessage(Long chatId) {
        log.info("Sending \"Care Pet Recommendation\" message to {}", chatId);
        try {
            SendPhoto sendPhoto;
            User user = userService.findUserByChatId(chatId);
            // выполняется проверка типа выбранного приюта для формирования конкретного списка рекомендаций
            if (user.getShelter().getType().equals(PetType.DOG)) {
                sendPhoto = specificMediaMessageCreator.createCareDogRecMessage(chatId);
            } else {
                sendPhoto = specificMediaMessageCreator.createCareCatRecMessage(chatId);
            }
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send \"Care Pet Recommendation\" message to {}", chatId, e);
        }
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "Начать оформление"
     */
//    после внесению данных о себе, пользователю будет присвоен статус POTENTIAL
    public void sendStartRegistrationMessage(Long chatId, String firstName) {
        log.info("Sending \"Start Registration\" message to {}", chatId);
        try {

//ФУНКЦИОНАЛ НАХОДИТСЯ В РАЗРАБОТКЕ. Roman

            // выполняется отправление сообщения с фото
//            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send \"Start Registration\" message to {}", chatId, e);
        }
    }


//    .........отправка сообщений пользователю на любые другие случаи........


    public void sendChooseShelterMessage(Long chatId) {

    }

    public void sendInfoForPotentialUserMessage(Long chatId) {

    }

    public void sendInfoForProbationUserMessage(Long chatId) {

    }

    public void sendReportPhotoMessage(Long chatId) {
        log.info("Sending report message to {}", chatId);
        LocalDateTime currentTime = LocalDateTime.now();
        SendPhoto sendPhoto;
        try {
            if(currentTime.toLocalTime().isAfter(LocalTime.of(18, 0))
               && currentTime.toLocalTime().isBefore(LocalTime.of(21, 0))){
                // объявляется переменная SendPhoto для конкретного сообщения
                sendPhoto = specificMediaMessageCreator.createReportSendTwoOptionsPhotoMessage(chatId);
                sendPhoto.replyMarkup(specificKeyboardCreator.fillOutReportActiveMessageKeyboard());
            } else {
                sendPhoto = specificMediaMessageCreator.createReportSendOneOptionPhotoMessage(chatId);
                sendPhoto.replyMarkup(specificKeyboardCreator.fillOutReportNotActiveMessageKeyboard());
            }
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
        }
    }
}
