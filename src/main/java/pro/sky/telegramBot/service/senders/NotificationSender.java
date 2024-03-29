package pro.sky.telegramBot.service.senders;

import com.pengrad.telegrambot.request.SendPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.service.executors.MessageExecutor;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.UserService;
import pro.sky.telegramBot.service.creators.keyboardCreators.SpecificKeyboardCreator;
import pro.sky.telegramBot.service.creators.mediaMessageCreators.SpecificMediaMessageCreator;

import javax.transaction.Transactional;

import static pro.sky.telegramBot.enums.UserState.PROBATION_REPORT;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j  // SLF4J logging
public class NotificationSender {
    private final SpecificMediaMessageCreator specificMediaMessageCreator;
    private final MessageExecutor executor;

    private final UserService userService;
    private final SpecificKeyboardCreator specificKeyboardCreator;

    /**
     * Метод формирует и отправляет сообщение о возможности заполнять отчет онлайн
     */
    public void sendNotificationToAdopterAboutDailyReportPhotoMessage(Long chatId) {
        log.info("Sending a message to the user than he daily should fill out a report {}", chatId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createNotificationToAdopterAboutDailyReportPhotoMessage(chatId);
            executor.executePhotoMessage(sendPhoto);
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
            executor.executePhotoMessage(sendPhoto);
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
            executor.executePhotoMessage(sendPhoto);
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
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send a message to the volunteer {} than no pet was detected by the adopter {}", volunteerChatId, userId, e);
        }
    }

    public void sendNotificationToAdopterAboutNeedToSendReportPhotoMessage(Long chatId) {
        log.info("Sending a message to the adopter {} than he should send a report", chatId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createNeedToSendReportPhotoMessage(chatId);
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.info("Failed to send a message to the adopter {} than he should send a report", chatId, e);
        }
    }

    public void sendNotificationToAdopterAboutNeedToSendPhotoForReportPhotoMessage(Long chatId) {
        log.info("Sending a message to the adopter {} than he should send a photo for the report", chatId);
        try {
            SendPhoto sendPhoto;
            sendPhoto = specificMediaMessageCreator.createNeedToSendPhotoForReportPhotoMessage(chatId);
            executor.executePhotoMessage(sendPhoto);
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
                    executor.executePhotoMessage(sendPhoto);
                    break;
                case "try your best":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToVolunteerAboutTryYourBest(chatId, userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                case "good job":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToVolunteerAboutGoodJob(chatId, userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                default:
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToVolunteerAboutCalculationsError(chatId, userChatId);
                    executor.executePhotoMessage(sendPhoto);
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
                    executor.executePhotoMessage(sendPhoto);
                    break;
                case "try your best":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToAdopterAboutTryYourBest(userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                case "good job":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToAdopterAboutGoodJob(userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.info("Failed to send a message to the user {} about his statistic", userChatId, e);
        }
    }

    public void sendNotificationToVolunteerAboutFinalCheck(String notificationAction, Long chatId, Long userChatId) {
        log.info("Sending a message to the Volunteer {} about adopter's statistic", chatId);
        try {
            SendPhoto sendPhoto;
            switch (notificationAction) {
                case "problem":
                    sendPhoto = specificMediaMessageCreator
                            .createUnsuccessfulNotificationToVolunteerPhotoMessage(chatId, userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                case "try your best":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToVolunteerAboutExtension(chatId, userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                case "good job":
                    sendPhoto = specificMediaMessageCreator.
                            createSuccessfulNotificationToVolunteer(chatId, userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                default:
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToVolunteerAboutCalculationsError(chatId, userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
            }
        } catch (Exception e) {
            log.info("Failed to send a message to the Volunteer {} about adopter's statistic", chatId, e);
        }
    }

    public void sendNotificationToAdopterAboutFinalCheck(String notificationAction, Long userChatId) {
        log.info("Sending a message to the user {} about his statistic", userChatId);
        try {
            SendPhoto sendPhoto;
            switch (notificationAction) {
                case "problem":
                    sendPhoto = specificMediaMessageCreator
                            .createUnsuccessfulNotificationToAdopterPhotoMessage(userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                case "try your best":
                    sendPhoto = specificMediaMessageCreator.
                            createNotificationToAdopterAboutExtension(userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                case "good job":
                    sendPhoto = specificMediaMessageCreator.
                            createSuccessfulNotificationToAdopter(userChatId);
                    executor.executePhotoMessage(sendPhoto);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.info("Failed to send a message to the user {} about his statistic", userChatId, e);
        }
    }

    /**
     * Метод формирует и отправляет сообщение пользователю,<br>
     * когда он заполняет отчет онлайн в боте
     */
//    метод перенесен из MessageSender
    public void sendQuestionForReportPhotoMessage(Long chatId, String question, int questionIdentifier, Long reportId) {
        log.info("Sending question photo message to {}", chatId);
        try {
            User user = userService.findUserByChatId(chatId);
            SendPhoto sendPhoto;
            if (user != null && user.getState().equals(PROBATION_REPORT)) {
                sendPhoto = specificMediaMessageCreator.createQuestionForReportMessage(chatId, question);
                sendPhoto.replyMarkup(specificKeyboardCreator.questionForReportMessageKeyboard(questionIdentifier, reportId));
            } else {
                sendPhoto = specificMediaMessageCreator.createReportAcceptedPhotoMessage(chatId);
                sendPhoto.replyMarkup(specificKeyboardCreator.buttonToSendPhotoKeyboard());
            }
            executor.executePhotoMessage(sendPhoto);
        } catch (Exception e) {
            log.error("Failed to send question photo message to {}", chatId, e);
        }
    }
}
