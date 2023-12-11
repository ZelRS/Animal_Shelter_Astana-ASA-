package pro.sky.telegramBot.sender.specificSenders;

import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j  // SLF4J logging
public class NotificationSender {
    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageExecutor messageExecutor;

    /**
     * Метод формирует и отправляет сообщение о возможности заполнять отчет онлайн
     */
    public void sendNotificationToAdopterAboutDailyReportPhotoMessage(Long chatId) {
        log.info("Sending a message to the user than he daily should fill out a report {}", chatId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createNotificationToAdopterAboutDailyReportPhotoMessage(chatId);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send a message to the user than he daily should fill out a report {}", chatId, e);
        }
    }
    /**
     * Метод формирует и отправляет сообщение о начале процедуры онлайн заполнения отчета
     */
    public void sendNotificationToAdopterAboutStartReportPhotoMessage(Long chatId) {
        log.info("Sending a message to {} about starting to fill out the report online", chatId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createNotificationToAdopterAboutStartReportPhotoMessage(chatId);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send a message to {} about starting to fill out the report online", chatId, e);
        }
    }

    /**
     * Метод формирует и отправляет сообщение о завершении процедуры онлайн заполнения отчета
     */
    public void sendNotificationToAdopterAboutEndReportPhotoMessage(Long chatId) {
        log.info("Sending a message to {} about finishing to fill out the report online", chatId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createNotificationToAdopterAboutEndReportPhotoMessage(chatId);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send a message to {} about finishing to fill out the report online", chatId, e);
        }
    }

    /**
     * Метод формирует и отправляет сообщение об отсутствии животного в базе в нового усыновителя
     */
    public void sendMissingPetMessageToVolunteerPhotoMessage(Long userId, Long volunteerChatId) {
        log.info("Sending a message to the volunteer {} than no pet was detected by the adopter {}", volunteerChatId, userId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createMissingPetMessageToVolunteerPhotoMessage(volunteerChatId, userId);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send a message to the volunteer {} than no pet was detected by the adopter {}", volunteerChatId, userId, e);
        }
    }

    public void sendNotificationToAdopterAboutNeedToSendReportPhotoMessage(Long chatId) {
        log.info("Sending a message to the adopter {} than he should send a report", chatId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createNeedToSendReportPhotoMessage(chatId);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send a message to the adopter {} than he should send a report", chatId, e);
        }
    }

    public void sendNotificationToAdopterAboutNeedToSendPhotoForReportPhotoMessage(Long chatId) {
        log.info("Sending a message to the adopter {} than he should send a photo for the report", chatId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createNeedToSendPhotoForReportPhotoMessage(chatId);
            messageExecutor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send a message to the adopter {} than he should send a photo for the report", chatId, e);
        }
    }

    public void sendNotificationToVolunteerAboutCheck(String notificationAction, Long chatId, Long userChatId) {
        log.info("Sending a message to the Volunteer {} about adopter's statistic", chatId);
        try {
            SendPhoto sendPhoto;
            switch (notificationAction) {
                case "problem":
                    sendPhoto = specificMediaMessageCreator
                            .createNotificationToVolunteerAboutProblemPhotoMessage(chatId, userChatId);
                    messageExecutor.executePhotoMessage(sendPhoto);
                    break;
                case "try your best":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToVolunteerAboutTryYourBest(chatId, userChatId);
                    messageExecutor.executePhotoMessage(sendPhoto);
                    break;
                case "good job":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToVolunteerAboutGoodJob(chatId, userChatId);
                    messageExecutor.executePhotoMessage(sendPhoto);
                    break;
                default:
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToVolunteerAboutCalculationsError(chatId, userChatId);
                    messageExecutor.executePhotoMessage(sendPhoto);
                    break;
            }
        } catch (Exception e) {
            log.info("Failed to send a message to the Volunteer {} about adopter's statistic", chatId, e);
        }
    }

    public void sendNotificationToAdopterAboutCheck(String notificationAction, Long userChatId) {
        log.info("Sending a message to the user {} about his statistic", userChatId);
        try {
            SendPhoto sendPhoto;
            switch (notificationAction) {
                case "problem":
                    sendPhoto = specificMediaMessageCreator
                            .createNotificationToAdopterAboutProblemPhotoMessage(userChatId);
                    messageExecutor.executePhotoMessage(sendPhoto);
                    break;
                case "try your best":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToAdopterAboutTryYourBest(userChatId);
                    messageExecutor.executePhotoMessage(sendPhoto);
                    break;
                case "good job":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToAdopterAboutGoodJob(userChatId);
                    messageExecutor.executePhotoMessage(sendPhoto);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.info("Failed to send a message to the user {} about his statistic", userChatId, e);
        }
    }
}
