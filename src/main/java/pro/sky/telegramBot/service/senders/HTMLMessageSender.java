package pro.sky.telegramBot.service.senders;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.config.MessageConfig;
import pro.sky.telegramBot.service.executors.MessageExecutor;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.ShelterService;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;
import pro.sky.telegramBot.service.creators.keyboardCreators.SpecificKeyboardCreator;

import javax.transaction.Transactional;

import static com.pengrad.telegrambot.model.request.ParseMode.HTML;
import static pro.sky.telegramBot.enums.UserState.*;


/**
 * методы класса группируют компоненты и формируют HTML-сообщения ответа,<br>
 * а затем вызывают {@link #executor}, который выполняет отправку<br>
 * этого HTML-сообщения
 */
@RequiredArgsConstructor
@Service
@Transactional
@Slf4j  // SLF4J logging
public class HTMLMessageSender {
    private final MessageConfig messageConfig;
    private final MessageExecutor executor;
    private final UserService userService;
    private final ShelterService shelterService;
    private final SpecificKeyboardCreator specificKeyboardCreator;

    /**
     * метод формирует и отправляет дефолтное сообщение в HTML формате
     */
    // дефолтное сообщение существует в качестве заглушки на случай, когда функционал не реализован
    public void sendDefaultHTMLMessage(Long chatId) {
        log.info("Sending about message to {}", chatId);
        SendMessage message = new SendMessage(String.valueOf(chatId),
                String.format(messageConfig.getMSG_DEFAULT())).parseMode(HTML);
        // выполняется отправление дефолтного сообщения в HTML формате
        executor.executeHTMLMessage(message);
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он нажал на кнопку "Начать оформление"
     */
    //  после внесению данных о себе, пользователю будет присвоен статус POTENTIAL
    public void sendStartRegistrationHTMLMessage(Long chatId) {
        log.info("Sending info_table sample document to {}", chatId);
        User user = userService.findUserByChatId(chatId);
        SendMessage message;
        if (!user.getState().equals(INVITED)) {
            message = new SendMessage(chatId, messageConfig.getMSG_START_REGISTRATION()).parseMode(HTML);
        } else {
            message = new SendMessage(chatId, "Вы уже прошли регистрацию в конкретном приюте.\n" +
                    "Если вы хотите отменить Вашу запись, свяжитесь с волонтером");
            message.replyMarkup(specificKeyboardCreator.pressTheButtonToCallVolunteerKeyboard());
        }
        executor.executeHTMLMessage(message);
    }

    /**
     * метод формирует и отправляет сообщение пользователю,<br>
     * когда он выбирает команду "/report", не имея на то полномочий
     */
    // выполняется отправление сообщения в HTML формате о невозможности выполнить команду
    // так как у пользователя нет доступа
    public void sendNotSupportedHTMLMessage(Long chatId) {
        log.info("Sending not supported message to {}", chatId);
        SendMessage message = new SendMessage(String.valueOf(chatId),
                String.format(messageConfig.getMSG_NOT_SUPPORTED())).parseMode(HTML);
        executor.executeHTMLMessage(message);
    }

    public void sendNoAdoptionRecordHTMLMessage(Long chatId) {
        log.info("Sending a no adoption record message to {}", chatId);
        try {
            SendMessage sendMessage = new SendMessage(chatId, messageConfig.getMSG_NO_ADOPTION_RECORD());
            executor.executeHTMLMessage(sendMessage);
        } catch (Exception e) {
            log.info("Failed to send a no adoption record message to {}", chatId, e);
        }
    }

    public void sendInfoMenuHTMLMessage(Long chatId, String msg) {
        SendMessage message = new SendMessage(chatId, msg);
        User user = userService.findUserByChatId(chatId);
        if (!user.getState().equals(VOLUNTEER))
            message.replyMarkup(specificKeyboardCreator.shelterInformationFunctionalKeyboard());
        executor.executeHTMLMessage(message);
    }

    public void sendReportNotAvailableHTMLMessage(Long chatId) {
        log.info("Sending a no report function available message to {}", chatId);
        try {
            SendMessage sendMessage = new SendMessage(chatId, messageConfig.getMSG_NO_REPORT_AVAILABLE());
            executor.executeHTMLMessage(sendMessage);
        } catch (Exception e) {
            log.info("Failed to send a no adoption record message to {}", chatId, e);
        }
    }

    public void sendStatisticAboutShelterHTMLMessage(Long chatId) {
        log.info("Sending Statistic To Volunteer {}", chatId);
        try {
            SendMessage sendMessage = new SendMessage(chatId, shelterService.getShelterNamesWitPetCounts().toString());
            executor.executeHTMLMessage(sendMessage);
        } catch (Exception e) {
            log.error(" Failed to send message");
        }
    }

    public void sendStatisticAboutNewUserHTMLMessage(Long chatId) {
        log.info("Sending Statistic To Volunteer  {} About New User", chatId);
        try {
            SendMessage sendMessage = new SendMessage(chatId, userService.getFREESateUser().toString());
            executor.executeHTMLMessage(sendMessage);
        } catch (Exception e) {
            log.error(" Failed to send message");
        }
    }

    public void sendAttachPetPhotoForReportHTMLMessage(Long chatId) {
        log.info("Was invoked method sendPetPhotoForReportMessage");
        try {
            SendMessage sendMessage = new SendMessage(chatId, "Вложите фотографию");
            User user = userService.findUserByChatId(chatId);
            if (user != null && user.getState().equals(PROBATION)) {
                user.setState(PROBATION_PHOTO);
                userService.update(user);
            }
            executor.executeHTMLMessage(sendMessage);
        } catch (Exception e) {
            log.error("Failed to send photo response message to {}", chatId, e);
        }
    }
}
