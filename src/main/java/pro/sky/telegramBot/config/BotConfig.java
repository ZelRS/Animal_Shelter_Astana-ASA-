package pro.sky.telegramBot.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * в этом классе формируется бин {@link #telegramBot()},<br>
 * а также инициализируются все текстовые переменные, данные которых<br>
 * берутся из application.properties
 */
@Configuration
@Data
public class BotConfig {
    // ПЕРЕМЕННАЯ С ТОКЕНОМ БОТА
    @Value("${telegram.bot.token}")
    private String token;

    // ПЕРЕМЕННЫЕ С ТЕКСТОМ ДЛЯ ЭНДПОИНТОВ, ПРЕДОСТАВЛЯЕМЫХ ПОЛЬЗОВАТЕЛЮ
    @Value("${REF_MSG_BACK}")
    private String REF_MSG_BACK;

    // ПЕРЕМЕННЫЕ С ТЕКСТОМ ДЛЯ ОТВЕТНЫХ СООБЩЕНИЙ ПРИВЕТСТВИЯ
    @Value("${MSG_SIMPLE_WELCOME}")
    private String MSG_SIMPLE_WELCOME;
    @Value("${MSG_UNTRUSTED_WELCOME}")
    private String MSG_SORRY_WELCOME;
    @Value("${MSG_BLOCKED_WELCOME}")
    private String MSG_BLOCKED_WELCOME;
    @Value("${MSG_NO_REPORT_AVAILABLE}")
    private String MSG_NO_REPORT_AVAILABLE;

    // ПЕРЕМЕННЫЕ С ТЕКСТОМ ДЛЯ ДРУГИХ ОТВЕТНЫХ СООБЩЕНИЙ
    @Value("${MSG_VOLUNTEER_NOTIFIED}")
    private String MSG_VOLUNTEER_NOTIFIED;
    @Value("${MSG_DEFAULT}")
    private String MSG_DEFAULT;
    @Value("${MSG_NOT_SUPPORTED}")
    private String MSG_NOT_SUPPORTED;
    @Value("${MSG_SHELTER_INTRO_NULL}")
    private String MSG_SHELTER_INTRO_NULL;
    @Value("${MSG_SHELTER_INTRO_ONE}")
    private String MSG_SHELTER_INTRO_ONE;
    @Value("${MSG_SHELTER_INTRO_TWO}")
    private String MSG_SHELTER_INTRO_TWO;
    @Value("${MSG_SHELTER_INTRO_THREE}")
    private String MSG_SHELTER_INTRO_THREE;
    @Value("${MSG_SHELTER_DEFAULT_PREVIEW}")
    private String MSG_SHELTER_DEFAULT_PREVIEW;
    @Value("${MSG_TAKING_PET}")
    private String MSG_TAKING_PET;
    @Value("${MSG_CARE_PET_REC}")
    private String MSG_CARE_PET_REC;
    @Value("${MSG_CARE_DOG_SPEC_REC}")
    private String MSG_CARE_DOG_SPEC_REC;
    @Value("${MSG_SEND_REPORT_TWO_OPTIONS}")
    private String MSG_SEND_REPORT_TWO_OPTIONS;
    @Value("${MSG_SEND_REPORT_ONE_OPTION}")
    private String MSG_SEND_REPORT_ONE_OPTION;
    @Value("${MSG_REPORT_ACCEPTED}")
    private String MSG_REPORT_ACCEPTED;
    @Value("${MSG_REPORT_NOT_ACCEPTED}")
    private String MSG_REPORT_NOT_ACCEPTED;
    @Value("${MSG_START_REGISTRATION}")
    private String MSG_START_REGISTRATION;
    @Value("${MSG_SAVING_USER_INFO_SUCCESS}")
    private String MSG_SAVING_USER_INFO_SUCCESS;
    @Value("${MSG_SAVING_USER_PERSONAL_DOCS_SCREENS_SUCCESS}")
    private String MSG_SAVING_USER_PERSONAL_DOCS_SCREENS_SUCCESS;
    @Value("${MSG_HELLO_VOLUNTEER}")
    private String MSG_HELLO_VOLUNTEER;
    @Value("${MSG_CHOOSE_SHELTER}")
    private String MSG_CHOOSE_SHELTER;
    @Value("${MSG_NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT}")
    private String MSG_NOTIFICATION_TO_ADOPTER_ABOUT_DAILY_REPORT;
    @Value("${MSG_NOTIFICATION_ABOUT_START_REPORTING}")
    private String MSG_NOTIFICATION_ABOUT_START_REPORTING;
    @Value("${MSG_NOTIFICATION_ABOUT_END_REPORTING}")
    private String MSG_NOTIFICATION_ABOUT_END_REPORTING;
    @Value("${MSG_MISSING_PET}")
    private String MSG_MISSING_PET;
    @Value("${MSG_NO_ADOPTION_RECORD}")
    private String MSG_NO_ADOPTION_RECORD;
    @Value("${MSG_PHOTO_ACCEPTED}")
    private String MSG_PHOTO_ACCEPTED;
    @Value("${MSG_PHOTO_NOT_ACCEPTED}")
    private String MSG_PHOTO_NOT_ACCEPTED;
    @Value("${MSG_GET_HELP_FROM_VOLUNTEER}")
    private String MSG_GET_HELP_FROM_VOLUNTEER;
    @Value("${MSG_NEED_TO_SEND_REPORT}")
    private String MSG_NEED_TO_SEND_REPORT;
    @Value("${MSG_NEED_TO_SEND_PHOTO_FOR_REPORT}")
    private String MSG_NEED_TO_SEND_PHOTO_FOR_REPORT;
    @Value("${MSG_NOTIFICATION_PROBLEM}")
    private String MSG_NOTIFICATION_PROBLEM;
    @Value("${MSG_NOTIFICATION_TRY_YOUR_BEST}")
    private String MSG_NOTIFICATION_TRY_YOUR_BEST;
    @Value("${MSG_NOTIFICATION_GOOD_JOB}")
    private String MSG_NOTIFICATION_GOOD_JOB;
    @Value("${MSG_NOTIFICATION_CALCULATION_ERROR}")
    private String MSG_NOTIFICATION_CALCULATION_ERROR;
    @Value("${MSG_NOTIFICATION_ADOPTER_ABOUT_PROBLEM}")
    private String MSG_NOTIFICATION_ADOPTER_ABOUT_PROBLEM;
    @Value("${MSG_NOTIFICATION_ADOPTER_ABOUT_TRY_YOUR_BEST}")
    private String MSG_NOTIFICATION_ADOPTER_ABOUT_TRY_YOUR_BEST;
    @Value("${MSG_NOTIFICATION_ADOPTER_ABOUT_GOOD_JOB}")
    private String MSG_NOTIFICATION_ADOPTER_ABOUT_GOOD_JOB;
    @Value("${MSG_NOTIFICATION_FAILED}")
    private String MSG_NOTIFICATION_FAILED;
    @Value("${MSG_NOTIFICATION_EXTENSION}")
    private String MSG_NOTIFICATION_EXTENSION;
    @Value("${MSG_NOTIFICATION_SUCCESSFUL}")
    private String MSG_NOTIFICATION_SUCCESSFUL;
    @Value("${MSG_NOTIFICATION_ADOPTER_FAILED}")
    private String MSG_NOTIFICATION_ADOPTER_FAILED;
    @Value("${MSG_NOTIFICATION_ADOPTER_EXTENSION}")
    private String MSG_NOTIFICATION_ADOPTER_EXTENSION;
    @Value("${MSG_NOTIFICATION_ADOPTER_SUCCESSFUL}")
    private String MSG_NOTIFICATION_ADOPTER_SUCCESSFUL;

    // ПЕРЕМЕННЫЕ С ТЕКСТОМ НА КНОПКАХ
    @Value("${BUT_WANT_DOG}")
    private String BUT_WANT_DOG;
    @Value("${BUT_WANT_CAT}")
    private String BUT_WANT_CAT;
    @Value("${BUT_GET_FULL_INFO}")
    private String BUT_GET_FULL_INFO;
    @Value("${BUT_TAKING_PET}")
    private String BUT_TAKING_PET;
    @Value("${BUT_SEND_REPORT}")
    private String BUT_SEND_REPORT;
    @Value("${BUT_SEND_PET_PHOTO}")
    private String BUT_SEND_PET_PHOTO;
    @Value("${BUT_CALL_VOLUNTEER}")
    private String BUT_CALL_VOLUNTEER;
    @Value("${BUT_CARE_PET_REC}")
    private String BUT_CARE_PET_REC;
    @Value("${BUT_START_REGISTRATION}")
    private String BUT_START_REGISTRATION;
    @Value("${BUT_FILL_OUT_REPORT_ON}")
    private String BUT_FILL_OUT_REPORT_ON;
    @Value("${BUT_FILL_OUT_REPORT_OFF}")
    private String BUT_FILL_OUT_REPORT_OFF;
    @Value("${BUT_MORE_INFORMATION}")
    private String BUT_MORE_INFORMATION;
    @Value("${BUT_GO_TO_MAIN}")
    private String BUT_GO_TO_MAIN;
    @Value("${BUT_GO_TO_SHELTER_SELECT}")
    private String BUT_GO_TO_SHELTER_SELECT;
    @Value("${BUT_STATISTIC_NEW_USER}")
    private String BUT_STATISTIC_NEW_USER;
    @Value("${BUT_STATISTIC_SHELTER}")
    private String BUT_STATISTIC_SHELTER;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
