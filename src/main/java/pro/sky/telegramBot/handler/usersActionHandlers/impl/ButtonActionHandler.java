package pro.sky.telegramBot.handler.usersActionHandlers.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.entity.Button;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.handler.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.handler.usersActionHandlers.ActionHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.ReportService;
import pro.sky.telegramBot.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class ButtonActionHandler implements ActionHandler {
    private final MessageSender messageSender;
    private final ReportService reportService;
    private final UserService userService;
    private final BlockedUserHandler blockedUserHandler;

    @FunctionalInterface
    interface Button {
        void run(String firstName, String lastName, Long chatId);
    }

    private final Map<String, Button> buttonMap = new HashMap<>();

    /**
     * при запуске приложения происходит наполнение {@link #buttonMap} с кнопками,<br>
     * при нажатии которых должен высылаться конкретный ответ
     */
    @PostConstruct
    public void init() {
// ОТСЮДА НАЧИНАЕТСЯ РАБОТА РОМАНА
        buttonMap.put(BUT_TAKING_PET.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_TAKING_PET button");
            messageSender.sendTakingPetPhotoMessage(chatId, firstName);
        });

        buttonMap.put(BUT_CARE_PET_REC.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_CARE_PET_RECOMMENDATIONS button");
            messageSender.sendCarePetRecMessage(chatId);
        });

        buttonMap.put(BUT_START_REGISTRATION.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_START_REGISTRATION button");
            messageSender.sendStartRegistrationMessage(chatId);
        });

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА ЮРИЯ ПЕТУХОВА
        //Обработчик кнопки "Отправить отчет", проверяется статус пользователя
        buttonMap.put(BUT_SEND_REPORT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed SEND_REPORT button");
            User user = userService.findUserByChatId(chatId);
            if (user != null) {
                UserState state = user.getState();
                if (state != null && state.equals(PROBATION)) {
                    messageSender.sendReportPhotoMessage(chatId);
                } else {
                    messageSender.sendDefaultHTMLMessage(chatId);
                }
            }
        });
        //Если кнопка "Отправить отчет" доступна, то меняется статус пользователя и инициируется заполнение отчета
        buttonMap.put(BUT_FILL_OUT_REPORT_ON.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed FILL_OUT_REPORT button");
            User user = userService.findUserByChatId(chatId);
            if (user != null) {
                UserState state = user.getState();
                if (state != null && state.equals(PROBATION)) {
                    log.info("Change of user state to PROBATION_REPORT");
                    user.setState(PROBATION_REPORT);
                    userService.update(user);
                    log.info("Was invoked method to fill out the report");
                    reportService.createReportOnline(chatId);
                } else {
                    messageSender.sendDefaultHTMLMessage(chatId);
                }
            }
        });
        //Кнопка сделана без ответа, так как отключена в нерабочее время
        buttonMap.put(BUT_FILL_OUT_REPORT_OFF.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed FILL_OUT_REPORT button");
        });

// ОТСЮДА НАЧИНАЕТСЯ РАБОТА АЛЕКСЕЯ
        buttonMap.put(BUT_CALL_VOLUNTEER.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed CALL_VOLUNTEER button");

            // внизу заглушка! там должен быть метод, который будет высылаиь ответ при нажатии кнопки "позвать волонтера"
            // на кнопки пока не нажимайте. прилложение завалится. этот метод закомментирован и не подходит под реализуцию,
            // пожалуйста не вводите его из комментов. реализуйте сюда свой send по вашему заданию

            // messageSender.sendShelterFunctionalPhotoMessage(chatId);
        });

//точка входа в информационное меню приюта
        buttonMap.put(BUT_GET_FULL_INFO.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed GET_FULL_INFO button");
            messageSender.sendShelterFullInfoHTMLMessage(firstName, lastName, chatId);
        });
        buttonMap.put(BUT_WANT_DOG.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed WANT_DOG button");
            messageSender.sendDogSheltersListPhotoMessage(chatId);
        });
        buttonMap.put(BUT_WANT_CAT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed WANT_CAT button");
            messageSender.sendCatSheltersListPhotoMessage(chatId);
        });

//возврат с навигационного меню в меню информации о приюте
        buttonMap.put(BUT_MORE_INFORMATION.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_MORE_INFORMATION button");
            messageSender.sendShelterFullInfoHTMLMessage(firstName, lastName, chatId);
        });

//Возврат в главное меню с навигационных кнопок
        buttonMap.put(BUT_GO_TO_MAIN.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_GO_TO_MAIN button");
            messageSender.sendShelterFunctionalPhotoMessage(chatId);
        });

//возврат к выбору приюта c навигационных кнопок
        buttonMap.put(BUT_GO_TO_SHELTER_SELECT.getCallbackData(), (firstName, lastName, chatId) -> {
            log.info("Pressed BUT_GO_TO_SHELTER_SELECT button");
            messageSender.sendFirstTimeWelcomePhotoMessage(firstName, chatId);
        });

    }

    /**
     * Метод ищет, есть ли в {@link #buttonMap} кнопка по ключу.
     * Если кнопка найдена, совершается логика, лежащая по значению этого ключа.
     * Если такой команды нет, отправляется дефолтное сообщение
     */
    @Override
    public void handle(String callbackData, String firstName, String lastName, Long chatId) {
        User user = userService.findUserByChatId(chatId);
        // Все нажатия кнопок пользователя с данным статусом проскакивают без обработки в класс заполнения отчета
        if (user != null && user.getState().equals(PROBATION_REPORT)) {
            log.info("Was invoked method of sending question by callbackData {} in handler", callbackData);
            reportService.fillOutReport(chatId, callbackData);
        } else if (user != null && user.getState().equals(BLOCKED)) {
            blockedUserHandler.sendBlockedWelcomePhotoMessage(chatId);
        }
        Button commandToRun = buttonMap.get(callbackData.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId);
        } else {
            log.warn("No handler found for button: {}", callbackData);
            // отправка дефолтного сообщения
            messageSender.sendDefaultHTMLMessage(chatId);
        }
    }
}
