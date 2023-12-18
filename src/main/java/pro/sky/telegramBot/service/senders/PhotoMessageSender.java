package pro.sky.telegramBot.service.senders;

import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.MessageConfig;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.service.executors.MessageExecutor;
import pro.sky.telegramBot.service.handlers.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;
import pro.sky.telegramBot.service.creators.keyboardCreators.SpecificKeyboardCreator;
import pro.sky.telegramBot.service.creators.mediaMessageCreators.SpecificDocumentMessageCreator;
import pro.sky.telegramBot.service.creators.mediaMessageCreators.SpecificMediaMessageCreator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import static pro.sky.telegramBot.enums.UserState.*;

/**
 * методы класса группируют компоненты и формируют фото-сообщения ответа,<br>
 * а затем вызывают {@link #executor}, который выполняет отправку<br>
 * этого фото-сообщения
 */
@RequiredArgsConstructor
@Service
@Slf4j  // SLF4J logging
public class PhotoMessageSender implements BlockedUserHandler {
    private final SpecificDocumentMessageCreator specificDocumentMessageCreator;
    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final SpecificKeyboardCreator specificKeyboardCreator;
    private final MessageExecutor executor;
    private final UserService userService;
    private final MessageConfig messageConfig;

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
            executor.executePhotoMessage(sendPhoto);
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
            executor.executePhotoMessage(sendPhoto);
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
            executor.executePhotoMessage(sendPhoto);
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
            executor.executePhotoMessage(sendPhoto);
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
                sendPhoto.caption(messageConfig.getMSG_SHELTER_DEFAULT_PREVIEW());
            }
            // внедряется клавиатура выбора действия пользователя c приютом
            sendPhoto.replyMarkup(specificKeyboardCreator.shelterFunctionalMessageKeyboard());
            // выполняется отправление сообщения с фото
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send shelter functional message to {}", chatId, e);
        }
    }

    /**
     * метод формирует и отправляет сообщение пользователю<br>
     * для предоставления подробной информации о приюте
     */
    public void sendShelterFullInfoPhotoMessage(String firstName, String lastName, Long chatId) {
        User user = userService.findUserByChatId(chatId);
        log.debug("Sending Shelter full information to user {} with ChatID {}", firstName + " " + lastName, chatId);
        StringBuilder caption = new StringBuilder();
        caption.append("Здравствуйте, <b>").append(firstName).append(" ").append(lastName).append("</b>.\n\n")
                .append("Мы рады вас приветствовать в приюте \"").append(user.getShelter().getName()).append("\"\n\n")
                .append("<b>Выберите один из пунктов:</b>\n")
                .append("/details - Прочитать детальную информацию о приюте\n")
                .append("/address - Узнать где находится приют\n")
                .append("/schedule - График работы приюта, и часы приема\n")
                .append("/schema - Посмотреть схему проезда\n")
                .append("/sec_phone - Контакты для оформления пропуска\n")
                .append("/safety - Правила техники безопасности\n")
                .append("/callMe - Оставить контакты для обратной связи\n");
        try {
            SendPhoto sendPhoto = specificMediaMessageCreator.createShelterFullInfoPhotoMessage(chatId);
            sendPhoto.caption(caption.toString());
            //Внес изменения Роман. Мне этот метод пригодился в конце своей логики, но там не нужны кнопки
            if (user.getState() != INVITED) {
                sendPhoto.replyMarkup(specificKeyboardCreator.shelterInformationMainKeyboard());
            }
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send info message to {}", chatId, e);
        }
    }

    /**
     * Метод отправляет пользователю сообщение об ошибке, если информация не найдена в базе
     */
    public void sendInformationNotFoundPhotoMessage(Long chatId) {
        try {
            log.error("DB data is null");
            SendPhoto error = specificMediaMessageCreator.createInformationNotFoundMessage(chatId);
            executor.executePhotoMessage(error);
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
            User user = userService.findUserByChatId(chatId);
            SendPhoto sendPhoto;
            // происходит проверка статуса пользователя на UNTRUSTED
            if (!user.getState().equals(UNTRUSTED)) {
                sendPhoto = specificMediaMessageCreator.createTakingPetPhotoMessage(chatId, firstName);
                sendPhoto.replyMarkup(specificKeyboardCreator.takingPetMessageKeyboard());
            } else {
                sendPhoto = specificMediaMessageCreator.createAnswerForUntrustedUserMessage(chatId, firstName);
            }
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send \"Taking Pet\" message to {}", chatId, e);
        }
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "Рекомендации и советы"
     */
    public void sendCarePetRecPhotoMessage(Long chatId) {
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
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send \"Care Pet Recommendation\" message to {}", chatId, e);
        }
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
        executor.executePhotoMessage(sendPhoto);
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "Позвать Волонтёра"
     */
    public void sendCallVolunteerPhotoMessage(Long chatId, String username) {
        try {
            List<User> users = userService.findAllByState(VOLUNTEER);
            SendMessage sendMessage;
            if (users.size() != 0) {
                Random random = new Random();
                int id = random.nextInt(users.size());
                sendMessage = new SendMessage(users.get(id).getChatId(),
                        "\uD83D\uDD34<b>ВНИМАНИЕ!</b>\uD83D\uDD34\nВас вызывает пользователь @" + username);
                log.info("\"Volunteer has already notified\" message was sent to user with chatId={}", chatId);
                SendPhoto sendPhoto = specificMediaMessageCreator.createCallVolunteerPhotoMessage(chatId);
                log.info("Notification was sent to volunteer wigth chatId={}", users.get(id).getChatId());
                executor.executePhotoMessage(sendPhoto);
            } else {
                sendMessage = new SendMessage(chatId,
                        "К сожалению, на данный момент у нас нет ни одного волонтера....");
                log.info("\"We have no volunteers now\" message was sent to user with chatId={}", chatId);
            }
            executor.executeHTMLMessage(sendMessage);
        } catch (Exception e) {
            log.info("Failed to send \"call a volunteer\" message to {}", chatId, e);
        }
    }

    /**
     * Метод для выбора типа животного
     */
    public void sendChooseShelterPhotoMessage(Long chatId) {
        log.info("Sending first time welcome message to {}", chatId);
        try {
            // объявляется переменная SendPhoto для конкретного сообщения
            SendPhoto sendPhoto = specificMediaMessageCreator.createChooseShelterPhotoMessage(chatId);
            // внедряется клавиатура для выбора животного
            sendPhoto.replyMarkup(specificKeyboardCreator.petSelectionMessageKeyboard());
            // выполняется отправление сообщения с фото
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}", chatId, e);
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
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send welcome message to {}: {}", firstName, chatId, e);
        }
    }

    /**
     * Метод формирует и отправляет сообщение пользователю о статусе полученного документа
     */
    public void sendReportDocumentFromUserResponsePhotoMessage(Document document, Long chatId) throws IOException {
        log.info("Was invoked method sendReportResponseMessage");
        try {
            SendPhoto sendPhoto = specificDocumentMessageCreator.createReportResponseMessage(chatId, document);
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }

    /**
     * Метод формирует и отправляет сообщение после отправки им Exel документа c контактными данными
     */
    public void sendInfoTableDocumentFromUserResponsePhotoMessage(Document document, Long chatId) throws IOException {
        log.info("Was invoked method sendInfoTableResponseMessage");
        try {
            SendPhoto sendPhoto = specificDocumentMessageCreator.createInfoTableResponseMessage(chatId, document);
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }

    /**
     * Метод формирует и отправляет сообщение после отправки им PDF файла со скринами его персональных документов
     */
    public void sendScreenPersonalDocumentsResponseMessage(Document document, Long chatId) throws IOException {
        log.info("Was invoked method sendScreenPersonalDocumentsResponseMessage");
        try {
            User user = userService.findUserByChatId(chatId);
            List<User> users = userService.findAllByState(UserState.VOLUNTEER);

            SendPhoto sendPhoto;
            sendPhoto = specificDocumentMessageCreator.createScreenPersonalDocumentsResponseMessage(chatId, document);
            if (!users.isEmpty()) {
                String caption = String.format(messageConfig.getMSG_SAVING_USER_PERSONAL_DOCS_SCREENS_SUCCESS(),
                        user.getShelter().getName());
                sendPhoto.caption(caption);
                sendPhoto.replyMarkup(specificKeyboardCreator.afterRegistrationFinalKeyboard());
                executor.executePhotoMessage(sendPhoto);
            }
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }

    public void sendPhotoResponsePhotoMessage(PhotoSize[] photo, Long chatId) {
        log.info("Was invoked method sendPhotoResponseMessage");
        try {
            SendPhoto sendPhoto = specificDocumentMessageCreator.createPhotoResponseMessage(chatId, photo);
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send response message to {}", chatId, e);
        }
    }
}
