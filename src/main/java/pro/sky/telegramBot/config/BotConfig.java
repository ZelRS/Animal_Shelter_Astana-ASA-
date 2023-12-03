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

    // ПЕРЕМЕННЫЕ С ТЕКСТОМ ДЛЯ ДРУГИХ ОТВЕТНЫХ СООБЩЕНИЙ
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

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
