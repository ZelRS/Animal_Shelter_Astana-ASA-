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
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.handler.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;
import pro.sky.telegramBot.utils.mediaUtils.MediaMessageCreator;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;
import static pro.sky.telegramBot.enums.MessageImage.SHELTER_INFORMATION_MSG_IMG;
import static pro.sky.telegramBot.enums.UserState.*;

/**
 * методы класса группируют компоненты и формируют сообщения ответа,<br>
 * а затем вызывают {@link #messageExecutor}, который выполняет отправку<br>
 * этого сообщения
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j  // SLF4J logging
public class MessageSender implements BlockedUserHandler {
    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageExecutor messageExecutor;
    private final SpecificKeyboardCreator specificKeyboardCreator;
    private final BotConfig config;
    private final UserService userService;
    private final MediaMessageCreator mediaMessageCreator;

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
     * имеющему статус BLOCKED("в черном списке")
     */
    public void sendBlockedWelcomePhotoMessage(Long chatId) {
        log.info("Sending blocked welcome message to {}", chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createBlockedWelcomePhotoMessage(chatId);
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
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
            SendPhoto sendPhoto = mediaMessageCreator.createPhotoMessage(params);
            //Внес изменения Роман. Мне этот метод пригодился в конце своей логики, но там не нужны кнопки
            if (user.getState() != INVITED) {
                sendPhoto.replyMarkup(specificKeyboardCreator.shelterInformationMainKeyboard());
            }
            messageExecutor.executePhotoMessage(sendPhoto);
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

    /**
     * Обработчик команд из информационного меню
     */
    public void menuInformationHandler(Long chatId, SendMessage message) {
        log.info("Sending text message to user with chatID = " + chatId);
        messageExecutor.executeHTMLMessage(message);
    }

    public void menuInformationHandler(Long chatId, SendPhoto message) {
        log.info("Sending media message to user with chatID = " + chatId);
        messageExecutor.executePhotoMessage(message);
    }

    /**
     * Метод отправляет пользователю сообщение об ошибке, если информация не найдена в базе
     */
    public void sendInformationNotFoundMessage(Long chatId) {
        try {
            log.error("DB data is null");
            SendPhoto error = specificMediaMessageCreator.createInformationNotFoundMessage(chatId);
            messageExecutor.executePhotoMessage(error);
        } catch (IOException e) {
            log.error("Critical error in method informationNotFoundMessage. Message has not been sent to user");
        }
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "взять животное"
     */
    public void sendTakingPetPhotoMessage(Long chatId, String firstName) {
        log.info("Sending \"Taking Pet\" message to {}", chatId);
        try {
            SendPhoto sendPhoto;
            // происходит проверка статуса пользователя на UNTRUSTED
            if (!untrustedUserCheck(chatId, firstName)) {
                sendPhoto = specificMediaMessageCreator.createTakingPetPhotoMessage(chatId, firstName);
                sendPhoto.replyMarkup(specificKeyboardCreator.takingPetMessageKeyboard());
                messageExecutor.executePhotoMessage(sendPhoto);
            }
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
        User user = userService.findUserByChatId(chatId);
        SendMessage message;
        if (!user.getState().equals(INVITED)) {
            message = new SendMessage(chatId, config.getMSG_START_REGISTRATION()).parseMode(HTML);
        } else {
            message = new SendMessage(chatId, "Вы уже прошли регистрацию в конкретном приюте.\n" +
                    "Если вы хотите отменить Вашу запись, свяжитесь с волонтером");
            message.replyMarkup(specificKeyboardCreator.pressTheButtonToCallVolunteerKeyboard());
        }
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
        SendPhoto sendPhoto = null;
        User user = userService.findUserByChatId(chatId);
        if (user != null && user.getState().equals(PROBATION) && user.getAdoptionRecord() != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            try {
                if (currentTime.toLocalTime().isAfter(LocalTime.of(18, 0))
                        && currentTime.toLocalTime().isBefore(LocalTime.of(21, 0))) {
                    sendPhoto = specificMediaMessageCreator.createReportSendTwoOptionsPhotoMessage(chatId);
                    sendPhoto.replyMarkup(specificKeyboardCreator.fillOutReportActiveMessageKeyboard());
                } else {
                    sendPhoto = specificMediaMessageCreator.createReportSendOneOptionPhotoMessage(chatId);
                    sendPhoto.replyMarkup(specificKeyboardCreator.fillOutReportNotActiveMessageKeyboard());
                }

            } catch (Exception e) {
                log.error("Failed to send welcome message to {}", chatId, e);
            }

        } else {
            try {
                sendPhoto = specificMediaMessageCreator.createAskVolunteerForHelpPhotoMessage(chatId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        messageExecutor.executePhotoMessage(sendPhoto);
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
            document.fileName("info_table.xlsx");
            messageExecutor.executeDocument(document);
        } catch (Exception e) {
            log.error("Failed to send info_table document message to {}", chatId, e);
        }

    }

    public void sendChooseShelterMessage(Long chatId) {
        log.info("Sending first time welcome message to {}", chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createChooseShelterPhotoMessage(chatId);
            // внедряется клавиатура для выбора животного
            sendPhoto.replyMarkup(specificKeyboardCreator.petSelectionMessageKeyboard());
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
        }

    }

    /**
     * Метод формирует и отправляет сообщение пользователю,<br>
     * когда он заполняет отчет онлайн в боте
     */
    public void sendQuestionForReportPhotoMessage(Long chatId, String question, int questionIdentifier, Long reportId) {
        log.info("Sending question photo message to {}", chatId);
        try {
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getState().equals(PROBATION_REPORT)) {
                SendPhoto sendPhoto = specificMediaMessageCreator.createQuestionForReportMessage(chatId, question);
                sendPhoto.replyMarkup(specificKeyboardCreator.questionForReportMessageKeyboard(questionIdentifier, reportId));
                messageExecutor.executePhotoMessage(sendPhoto);
            } else {
                SendPhoto sendPhoto = specificMediaMessageCreator.createReportAcceptedPhotoMessage(chatId);
                messageExecutor.executePhotoMessage(sendPhoto);
            }
        } catch (Exception e) {
            log.error("Failed to send question photo message to {}", chatId, e);
        }
    }

    /**
     * Метод формирует и отправляет приветственное фото-сообщение волонтеру,<br>
     */

    public void sendVolunteerWelcomePhotoMessage(String firstName, Long chatId) {
        log.info("Sending sorry welcome message to {}: {}", firstName, chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createVolunteerWelcomePhotoMessage(chatId, firstName);
            // внедряется клавиатура для выбора продолжения
            sendPhoto.replyMarkup(specificKeyboardCreator.volunteerMenuMessageKeyboard());
            // выполняется отправление сообщения с фото
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}: {}", firstName, chatId, e);
        }
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "Позвать Волонтёра"
     */
    public void sendCallVolunteerPhotoMessage(Long chatId, String username) {
        log.info("Sending a message to the user \"call a volunteer\" {}", chatId);
        try {
            List<User> users = userService.findAllByState(VOLUNTEER);
            SendMessage sendMessage;
            if (users.size() != 0) {
                Random random = new Random();
                int id = random.nextInt(users.size());
                sendMessage = new SendMessage(users.get(id).getChatId(),
                        "\uD83D\uDD34<b>ВНИМАНИЕ!</b>\uD83D\uDD34\nВас вызывает пользователь @" + username);
                SendPhoto sendPhoto = specificMediaMessageCreator.createCallVolunteerPhotoMessage(chatId);
                messageExecutor.executePhotoMessage(sendPhoto);
            } else {
                sendMessage = new SendMessage(chatId,
                        "К сожалению, на данный момент у нас нет ни одного волонтера....");
            }
            messageExecutor.executeHTMLMessage(sendMessage);


        } catch (Exception e) {
            log.info("Failed to send \"call a volunteer\" message to {}", chatId, e);
        }
    }

    public void sendNoAdoptionRecordMessage(Long chatId) {
        log.info("Sending a no adoption record message to {}", chatId);
        try {
            SendMessage sendMessage = new SendMessage(chatId, config.getMSG_NO_ADOPTION_RECORD());
            messageExecutor.executeHTMLMessage(sendMessage);
        } catch (Exception e) {
            log.info("Failed to send a no adoption record message to {}", chatId, e);
        }
    }

    public void sendTextMessageFromInfoMenu(Long chatId, String msg) {
        SendMessage message;
        message = new SendMessage(chatId, msg);
        message.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        menuInformationHandler(chatId, message);
    }

    /**
     * проверка пользователя на статус "не надежный" c высылкой сообщения, что функционал ограничен
     */
    private boolean untrustedUserCheck(Long chatId, String firstName) throws IOException {
        User user = userService.findUserByChatId(chatId);
        SendPhoto sendPhoto;
        if (user.getState().equals(UNTRUSTED)) {
            sendPhoto = specificMediaMessageCreator.createAnswerForUntrustedUserMessage(chatId, firstName);
            messageExecutor.executePhotoMessage(sendPhoto);
            return true;
        }
        return false;
    }

    //    .........отправка сообщений пользователю на любые другие случаи........
}
