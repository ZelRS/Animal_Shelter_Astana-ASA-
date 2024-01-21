package pro.sky.telegramBot.service.handlers.usersActionHandlers.impl;

import com.pengrad.telegrambot.request.SendPhoto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.service.executors.MessageExecutor;
import pro.sky.telegramBot.service.handlers.specificHandlers.BlockedUserHandler;
import pro.sky.telegramBot.service.handlers.specificHandlers.impl.ShelterCommandHandler;
import pro.sky.telegramBot.service.handlers.specificHandlers.impl.WelcomeMessageHandler;
import pro.sky.telegramBot.service.handlers.usersActionHandlers.CommandActionHandler;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.senders.DocumentSender;
import pro.sky.telegramBot.service.senders.PhotoMessageSender;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.ShelterService;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;
import pro.sky.telegramBot.service.creators.keyboardCreators.SpecificKeyboardCreator;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static pro.sky.telegramBot.enums.Command.*;
import static pro.sky.telegramBot.enums.UserState.BLOCKED;
import static pro.sky.telegramBot.enums.UserState.PROBATION;

/**
 * класс для обработки сообщения, которое должно быть выслано пользователю<br>
 * при отправке им какой-либо определенной команды
 */
@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // SLF4J logging
public class CommandActionHandlerImpl implements CommandActionHandler {
    private final pro.sky.telegramBot.service.senders.HTMLMessageSender HTMLMessageSender;
    private final PhotoMessageSender photoMessageSender;
    private final DocumentSender documentSender;
    private final WelcomeMessageHandler welcomeMessageHandler;
    private final ShelterService shelterService;
    private final UserService userService;
    private final BlockedUserHandler blockedUserHandler;
    private final ShelterCommandHandler shelterCommandHandler;
    private final SpecificKeyboardCreator specificKeyboardCreator;
    private final MessageExecutor executor;

    @FunctionalInterface
    interface Command {
        void run(String firstName, String lastName, Long chatId, UserState userState);
    }

    private final Map<String, Command> commandMap = new HashMap<>();

    /**
     * при запуске приложения происходит наполнение {@link #commandMap} с командами,<br>
     * при получении которых должен высылаться конкретный ответ
     */
    @PostConstruct
    public void init() {

        commandMap.put(START.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received START command");
            welcomeMessageHandler.handleStartCommand(firstName, chatId, userState);
        });

        commandMap.put(REPORT.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received REPORT command");
            if (userState.equals(PROBATION)) {
                documentSender.sendReportToUserDocument(chatId);
            } else {
                HTMLMessageSender.sendNotSupportedHTMLMessage(chatId);
            }
        });

        commandMap.put(INFO_TABLE.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received INFO_TABLE command");
            documentSender.sendInfoTableToUserDocumentRequestMessage(chatId);
        });

        //Меню для дополнительной информации по приюту
        //Узнать дополнительную информацию о приюте
        commandMap.put(DETAILS.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received /details command");
            User user = userService.findUserByChatId(chatId);
            if (user.getShelter().getDescription() != null) {
                HTMLMessageSender.sendInfoMenuHTMLMessage(chatId, user.getShelter().getDescription());
            } else {
                photoMessageSender.sendInformationNotFoundPhotoMessage(chatId);
            }
        });

        // Получить адрес приюта
        commandMap.put(ADDRESS.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received /address command");
            User user = userService.findUserByChatId(chatId);
            if (user.getShelter().getAddress() != null) {
                HTMLMessageSender.sendInfoMenuHTMLMessage(chatId, user.getShelter().getAddress());
            } else {
                photoMessageSender.sendInformationNotFoundPhotoMessage(chatId);
            }
        });

//         Получить график работы приюта
        commandMap.put(SCHEDULE.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received /schedule command");
            User user = userService.findUserByChatId(chatId);
            if (user.getShelter().getSchedule() != null) {
                HTMLMessageSender.sendInfoMenuHTMLMessage(chatId, user.getShelter().getSchedule());
            } else {
                photoMessageSender.sendInformationNotFoundPhotoMessage(chatId);
            }
        });

//         Посмотреть схему проезда к приюту
        commandMap.put(SCHEMA.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received /schema command");
            User user = userService.findUserByChatId(chatId);
            if (user.getShelter().getSchema() != null) {
                SendPhoto sendPhoto = new SendPhoto(chatId, user.getShelter().getSchema());
                sendPhoto.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
                executor.executePhotoMessage(sendPhoto);
            } else {
                photoMessageSender.sendInformationNotFoundPhotoMessage(chatId);
            }
        });

//         Узнать номер телефона охраны для оформления пропуска
        commandMap.put(SEC_PHONE.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received /sec_phone command");
            User user = userService.findUserByChatId(chatId);
            if (user.getShelter().getSecurityPhone() != null) {
                HTMLMessageSender.sendInfoMenuHTMLMessage(chatId, user.getShelter().getSecurityPhone());
            } else {
                photoMessageSender.sendInformationNotFoundPhotoMessage(chatId);
            }
        });

//         Прочитать правила техники безопасности приюта
        commandMap.put(SAFETY.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received /safety command");
            User user = userService.findUserByChatId(chatId);
            if (user.getShelter().getSafetyRules() != null) {
                HTMLMessageSender.sendInfoMenuHTMLMessage(chatId, user.getShelter().getSafetyRules());
            } else {
                photoMessageSender.sendInformationNotFoundPhotoMessage(chatId);
            }
        });

//         Оставить заявку на обратный звонок
        commandMap.put(CALL_ME.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received /call_Me command");
            User user = userService.findUserByChatId(chatId);
            Optional<String> phoneFromDatabase = userService.getUserPhone(user.getId());
            phoneFromDatabase.ifPresentOrElse(phone -> {
                Long volunteerId = userService.getRandomVolunteerId();
                if (volunteerId != 0L) {
                    HTMLMessageSender.sendInfoMenuHTMLMessage(volunteerId, "Здравствуйте. Пользователь <b>" + user.getUserName() +
                            "</b> запросил обратный звонок.\nПерезвоните по номеру телефона " + phone);
                    HTMLMessageSender.sendInfoMenuHTMLMessage(user.getChatId(), "Спасибо! Наш сотрудник свяжется с вами в ближайшее время");
                } else {
                    log.error("There are no volunteers in database");
                    HTMLMessageSender.sendInfoMenuHTMLMessage(chatId, "К сожалению на данный момент у нас нет свободных волонтеров");
                }
            }, () -> HTMLMessageSender.sendInfoMenuHTMLMessage(chatId, "К сожалению вы не предоставили свой номер телефона." +
                    " Добавьте номер телефона в таком формате:\n/phone ##(###)###-##-##"));
        });

        // Связаться с волонтером
        commandMap.put(VOLUNTEER.getName(), (firstName, lastName, chatId, userState) -> {
            log.info("Received /volunteer command");
            photoMessageSender.sendShelterFullInfoPhotoMessage(firstName, lastName, chatId);
        });

        int refRecDocCount = 9; // число равно максимальному количеству(n) документов в /{n}rec в application.properties
        for (int i = 1; i <= refRecDocCount; i++) {

            String command = "/" + i + "rec";
            int refNum = i;
            commandMap.put(command, (firstName, lastName, chatId, userState) -> {
                log.info("Received getting {} RecDoc file command", refNum);
                documentSender.sendRecDocDocument(refNum, chatId);
            });
        }
    }

    /**
     * Метод ищет, есть ли в {@link #commandMap} кнопка по ключу.
     * Если кнопка найдена, совершается логика, лежащая по значению этого ключа.
     * Если такой команды нет, отправляется дефолтное сообщение
     */
    @Override
    public void handle(String command, String firstName, String lastName, Long chatId, UserState userState) {
        if (userState.equals(BLOCKED)) {
            blockedUserHandler.sendBlockedWelcomePhotoMessage(chatId);
            return;
        }
        if (command.startsWith("/phone")) {
            String phone = command.split(" ")[1];
            userService.addPhoneNumberToPersonInfo(firstName, lastName, chatId, phone);
            HTMLMessageSender.sendInfoMenuHTMLMessage(chatId, "Ваш номер телефона успешно добавлен");
            return;
        }
        if (command.matches("^/\\d+_((DOG)|(CAT))$")) {
            shelterCommandHandler.handle(command, firstName, lastName, chatId);
            return;
        }
        Command commandToRun = commandMap.get(command.toLowerCase());
        if (commandToRun != null) {
            commandToRun.run(firstName, lastName, chatId, userState);
        } else {
            log.warn("No handler found for command: {}", command);
            // отправка дефолтного сообщения
            HTMLMessageSender.sendDefaultHTMLMessage(chatId);
        }
    }
}
