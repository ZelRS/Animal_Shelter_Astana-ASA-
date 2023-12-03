package pro.sky.telegramBot.sender;


import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.entity.MediaMessageParams;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.volunteer.Volunteer;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.service.VolunteerService;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;
import pro.sky.telegramBot.utils.mediaUtils.MediaMessageCreator;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;
import static pro.sky.telegramBot.enums.MessageImage.SHELTER_INFORMATION_MSG_IMG;

/**
 * методы класса группируют компоненты и формируют сообщения ответа,<br>
 * а затем вызывают {@link #messageExecutor}, который выполняет отправку<br>
 * этого сообщения
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j  // SLF4J logging
public class MessageSender {
    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageExecutor messageExecutor;
    private final SpecificKeyboardCreator specificKeyboardCreator;
    private final BotConfig config;
    private final UserService userService;
    private final MediaMessageCreator mediaMessageCreator;
    private final VolunteerService volunteerService;
//    private final MediaLoader mediaLoader;

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
        User user = userService.findUserByChatId(chatId);
        log.debug("Sending Shelter full information to user {} with ChatID {}", firstName + " " + lastName, chatId);
        StringBuilder caption = new StringBuilder();
        caption.append("Здравствуйте, <b>").append(firstName).append(" ").append(lastName).append("</b>.\n\n")
                .append("Мы рады вас приветствовать в приюте \"").append(user.getShelter().getName()).append("\"\n\n")
                .append(getMenuBuilder());
        try {
            MediaMessageParams params = new MediaMessageParams();
            params.setChatId(chatId);
            params.setFilePath(SHELTER_INFORMATION_MSG_IMG.getPath());
            params.setCaption(caption.toString());
            messageExecutor.executePhotoMessage(mediaMessageCreator.createPhotoMessage(params)
                    .replyMarkup(specificKeyboardCreator.shelterInformationMainKeyboard()));
        } catch (Exception e) {
            log.error("Failed to send info message to {}", chatId, e);
        }
    }

    /**
     * метод формирует строку для отображения информационного меню
     */
    @NotNull
    private static StringBuilder getMenuBuilder() {
        StringBuilder menuMessage = new StringBuilder();
        menuMessage.append("<b>Выберите один из пунктов:</b>\n");
        menuMessage.append("/details - Прочитать детальную информацию о приюте\n");
        menuMessage.append("/address - Узнать где находится приют\n");
        menuMessage.append("/schedule - График работы приюта, и часы приема\n");
        menuMessage.append("/schema - Посмотреть схему проезда\n");
        menuMessage.append("/sec_phone - Контакты для оформления пропуска\n");
        menuMessage.append("/safety - Правила техники безопасности\n");
        menuMessage.append("/callMe - Оставить контакты для обратной связи\n");
        return menuMessage;
    }

//    Мне очень не нравится одинаковый код в методах ниже. Я потом сделаю рефактор чтоб было красиво.
//    Юра, не надо подсказывать!!! Я хочу сам придумать :-)

    /**
     * Метод отображает дополнительную информацию о приюте
     */
    public void sendShelterDetailsMessage(Long chatId) {
        User user = userService.findUserByChatId(chatId);
        SendMessage message = new SendMessage(chatId, user.getShelter().getDescription());
        message.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        messageExecutor.executeHTMLMessage(message);
    }

    /**
     * Метод отображает адрес приюта
     */
    public void sendShelterAddressMessage(Long chatId) {
        User user = userService.findUserByChatId(chatId);
        SendMessage message = new SendMessage(chatId, user.getShelter().getAddress());
        message.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        messageExecutor.executeHTMLMessage(message);
    }

    /**
     * Метод отображает график работы приюта
     */
    public void sendShelterScheduleMessage(Long chatId) {
        User user = userService.findUserByChatId(chatId);
        SendMessage message = new SendMessage(chatId, user.getShelter().getSchedule());
        message.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        messageExecutor.executeHTMLMessage(message);
    }

    /**
     * Метод отображает схему проезда к приюту
     */
    public void sendShelterSchemaMessage(Long chatId) {
        User user = userService.findUserByChatId(chatId);
        SendPhoto sendPhoto = new SendPhoto(chatId, user.getShelter().getSchema());
        sendPhoto.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        messageExecutor.executePhotoMessage(sendPhoto);
    }

    /**
     * Метод отображает номер телефона охраны для оформления пропуска
     */
    public void sendShelterSecurityPhoneMessage(Long chatId) {
        User user = userService.findUserByChatId(chatId);
        SendMessage message = new SendMessage(chatId, user.getShelter().getSecurityPhone());
        message.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        messageExecutor.executeHTMLMessage(message);
    }

    /**
     * Метод отображает правила техники безоасности для даного приюта
     */
    public void sendShelterSafetyRuleMessage(Long chatId) {
        User user = userService.findUserByChatId(chatId);
        SendMessage message = new SendMessage(chatId, user.getShelter().getSafetyRules());
        message.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        messageExecutor.executeHTMLMessage(message);
    }

    /**
     * Метод создает реквест волонтеру для обратного звонка пользователю
     */
    public void callMeBackRequest(Long chatId) {
        User user = userService.findUserByChatId(chatId);
        SendMessage message = new SendMessage(chatId, user.getShelter().getSafetyRules());
        message.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        messageExecutor.executeHTMLMessage(message);
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
    public void sendStartRegistrationMessage(Long chatId) {
        log.info("Sending info_table sample document to {}", chatId);
        SendMessage message = new SendMessage(chatId, config.getMSG_START_REGISTRATION()).parseMode(HTML);
        message.replyMarkup(specificKeyboardCreator.startRegistrationMessageKeyBoard());
        messageExecutor.executeHTMLMessage(message);
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "отправить отчет"
     */
// Проверяется текущее время, и в интервале с 18 до 21 пользователю предлагается заполнить отчет в боте
// или скачать документ для заполнения. В другое время доступна только функция скачивания документа.
    public void sendReportPhotoMessage(Long chatId) {
        log.info("Sending report message to {}", chatId);
        LocalDateTime currentTime = LocalDateTime.now();
        SendPhoto sendPhoto;
        try {
            if (currentTime.toLocalTime().isAfter(LocalTime.of(18, 0))
                    && currentTime.toLocalTime().isBefore(LocalTime.of(21, 0))) {
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

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он выбирает команду "/report", если пользователь взял питомца
     */
    // пользователь получает документ в формате xlsx для заполнения отчета
    // у пользователя должен быть статус PROBATION
    public void sendReportToUserDocumentMessage(Long chatId) {
        log.info("Sending report document message to {}", chatId);
        SendDocument sendDocument = null;
        try {
            sendDocument = specificMediaMessageCreator.createReportSendDocumentMessage(chatId);
        } catch (Exception e) {
            log.error("Failed to send report document message to {}", chatId, e);
        }

        messageExecutor.executeDocument(sendDocument);
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он выбирает команду "/report", не имея на то полномочий
     */
    // выполняется отправление сообщения в HTML формате о невозможности выполнить команду
    // так как у пользователя нет доступа
    public void sendNotSupportedMessage(Long chatId) {
        log.info("Sending not supported message to {}", chatId);
        SendMessage message = new SendMessage(String.valueOf(chatId),
                String.format(config.getMSG_NOT_SUPPORTED())).parseMode(HTML);
        messageExecutor.executeHTMLMessage(message);
    }

    /**
     * метод формирует и отправляет файл пользователю,<br>
     * когда он нажимает на ссылку рядом с выбранным документом
     */
    public void sendRecDocDocumentMessage(Integer refNum, Long chatId) {
        log.info("Sending recommendation document to {}", chatId);
        try {
            SendDocument sendDoc = specificMediaMessageCreator.createRecDocDocumentMessage(refNum, chatId);
            messageExecutor.executeDocument(sendDoc);
        } catch (Exception e) {
            log.error("Failed to send recommendation document to {}", chatId, e);
        }
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он выбирает команду "/info_table", если пользователь взял питомца
     */
    // пользователь получает документ в формате xlsx для введения контактных данных
    public void sendInfoTableToUserDocumentMessage(Long chatId) {
        log.info("Sending info_table document message to {}", chatId);
        try {
            SendDocument document;
            document = specificMediaMessageCreator.createInfoTableDocumentMessage(chatId);
            messageExecutor.executeDocument(document);
        } catch (Exception e) {
            log.error("Failed to send info_table document message to {}", chatId, e);
        }

    }


    //    .........отправка сообщений пользователю на любые другие случаи........


    public void sendChooseShelterMessage(Long chatId) {

    }

    public void sendInfoForPotentialUserMessage(Long chatId) {

    }

    public void sendInfoForProbationUserMessage(Long chatId) {


    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "Позвать Волонтёра"
     */
    public void sendCallVolunteerPhotoMessage(Long chatId) {
        log.info("Sending a message to the user \"call a volunteer\" {}", chatId );
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createCallVolunteerPhotoMessage(chatId);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send \"call a volunteer\" message to {}", chatId, e);
        }
    }
}