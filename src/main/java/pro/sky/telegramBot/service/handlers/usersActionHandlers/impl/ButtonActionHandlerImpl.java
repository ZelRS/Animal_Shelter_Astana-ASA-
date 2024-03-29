package pro.sky.telegramBot.service.handlers.usersActionHandlers.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.service.handlers.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.service.handlers.usersActionHandlers.ButtonActionHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.senders.HTMLMessageSender;
import pro.sky.telegramBot.service.senders.PhotoMessageSender;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.ReportService;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static pro.sky.telegramBot.entity.Button.CallbackData.*;
import static pro.sky.telegramBot.enums.UserState.*;

/**
 * класс для обработки сообщения, которое должно быть выслано пользователю<br>
 * при его нажатии на определенную кнопку
 */
@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class ButtonActionHandlerImpl implements ButtonActionHandler {
    private final ReportService reportService;
    private final UserService userService;
    private final BlockedUserHandler blockedUserHandler;
    private final PhotoMessageSender photoMessageSender;
    private final HTMLMessageSender HTMLMessageSender;
    private final Map<String, Button> buttonMap = new HashMap<>();

    /**
     * при запуске приложения происходит наполнение {@link #buttonMap} с кнопками,<br>
     * при нажатии которых должен высылаться конкретный ответ
     */
    @PostConstruct
    public void init() {
        buttonMap.put(BUT_TAKING_PET.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed BUT_TAKING_PET button");
            photoMessageSender.sendTakingPetPhotoMessage(chatId, firstName);
        });

        buttonMap.put(BUT_CARE_PET_REC.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed BUT_CARE_PET_RECOMMENDATIONS button");
            photoMessageSender.sendCarePetRecPhotoMessage(chatId);
        });

        buttonMap.put(BUT_START_REGISTRATION.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed BUT_START_REGISTRATION button");
            HTMLMessageSender.sendStartRegistrationHTMLMessage(chatId);
        });

        //  Обработчик кнопки "Отправить отчет", проверяется статус пользователя
        buttonMap.put(BUT_SEND_REPORT.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed SEND_REPORT button");
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getAdoptionRecord() != null && userState.equals(PROBATION)) {
                photoMessageSender.sendReportPhotoMessage(chatId);
                return;
            }
            HTMLMessageSender.sendReportNotAvailableHTMLMessage(chatId);
        });

        buttonMap.put(BUT_SEND_PET_PHOTO.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed SEND_PET_PHOTO button");
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getAdoptionRecord() != null && userState.equals(PROBATION)) {
                HTMLMessageSender.sendAttachPetPhotoForReportHTMLMessage(chatId);
                return;
            }
            HTMLMessageSender.sendDefaultHTMLMessage(chatId);
        });

        //  Если кнопка "Отправить отчет" доступна, то меняется статус пользователя и инициируется заполнение отчета
        buttonMap.put(BUT_FILL_OUT_REPORT_ON.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed FILL_OUT_REPORT button");
            User user = userService.findUserByChatId(chatId);
            if (user != null && userState.equals(PROBATION) && user.getAdoptionRecord() != null) {
                log.info("Change of user state to PROBATION_REPORT");
                user.setState(PROBATION_REPORT);
                userService.update(user);
                log.info("Was invoked method to fill out the report");
                reportService.createReportOnline(chatId);
                return;
            }
            HTMLMessageSender.sendDefaultHTMLMessage(chatId);
        });

        //  Кнопка сделана без ответа, так как отключена в нерабочее время
        buttonMap.put(BUT_FILL_OUT_REPORT_OFF.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed FILL_OUT_REPORT button");
        });

        //  Точка входа "позвать волонтёра"
        buttonMap.put(BUT_CALL_VOLUNTEER.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed CALL_VOLUNTEER button");
            photoMessageSender.sendCallVolunteerPhotoMessage(chatId, username);
        });

        //  Точка входа в информационное меню приюта
        buttonMap.put(BUT_GET_FULL_INFO.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed GET_FULL_INFO button");
            photoMessageSender.sendShelterFullInfoPhotoMessage(firstName, lastName, chatId);
        });

        buttonMap.put(BUT_WANT_DOG.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed WANT_DOG button");
            photoMessageSender.sendDogSheltersListPhotoMessage(chatId);
        });

        buttonMap.put(BUT_WANT_CAT.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed WANT_CAT button");
            photoMessageSender.sendCatSheltersListPhotoMessage(chatId);
        });

        //  Возврат с навигационного меню в меню информации о приюте
        buttonMap.put(BUT_MORE_INFORMATION.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed BUT_MORE_INFORMATION button");
            photoMessageSender.sendShelterFullInfoPhotoMessage(firstName, lastName, chatId);
        });

        //  Возврат в главное меню с навигационных кнопок
        buttonMap.put(BUT_GO_TO_MAIN.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed BUT_GO_TO_MAIN button");
            photoMessageSender.sendShelterFunctionalPhotoMessage(chatId);
        });

        //  Возврат к выбору приюта c навигационных кнопок
        buttonMap.put(BUT_GO_TO_SHELTER_SELECT.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed BUT_GO_TO_SHELTER_SELECT button");
            photoMessageSender.sendFirstTimeWelcomePhotoMessage(firstName, chatId);
        });
        //статистика по приютам
        buttonMap.put(BUT_STATISTIC_SHELTER.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed BUT_STATISTIC_SHELTER button");
            HTMLMessageSender.sendStatisticAboutShelterHTMLMessage(chatId);
        });

        //статистика по пользователям
        buttonMap.put(BUT_STATISTIC_NEW_USER.getCallbackData(), (firstName, lastName, chatId, username, userState) -> {
            log.info("Pressed BUT_STATISTIC_NEW_USER button");
            HTMLMessageSender.sendStatisticAboutNewUserHTMLMessage(chatId);
        });
    }

    /**
     * Метод ищет, есть ли в {@link #buttonMap} кнопка по ключу.
     * Если кнопка найдена, совершается логика, лежащая по значению этого ключа.
     * Если такой команды нет, отправляется дефолтное сообщение
     */
    @Override
    public void handle(String callbackData, String firstName, String lastName, Long chatId, String username, UserState userState) {
        // Все нажатия кнопок пользователя с данным статусом проскакивают без обработки в класс заполнения отчета
        if (userState.equals(PROBATION_REPORT)) {
            log.info("Was invoked method of sending question by callbackData {} in handler", callbackData);
            reportService.fillOutReport(chatId, callbackData);
            return;
        } else if (userState.equals(BLOCKED)) {
            blockedUserHandler.sendBlockedWelcomePhotoMessage(chatId);
            return;
        }
        Button commandToRun = buttonMap.get(callbackData.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId, username, userState);
        } else {
            log.warn("No handler found for button: {}", callbackData);
            // отправка дефолтного сообщения
            HTMLMessageSender.sendDefaultHTMLMessage(chatId);
        }
    }

    @FunctionalInterface
    interface Button {
        void run(String firstName, String lastName, Long chatId, String username, UserState userState);
    }
}
