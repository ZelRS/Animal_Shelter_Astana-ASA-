package pro.sky.telegramBot.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    // ПЕРЕМЕННЫЕ С ТЕКСТОМ НА КНОПКАХ
    @Value("${BUT_WANT_DOG}")
    private String BUT_WANT_DOG;
    @Value("${BUT_WANT_CAT}")
    private String BUT_WANT_CAT;
    @Value("${BUT_GET_FULL_INFO}")
    private String BUT_GET_FULL_INFO;
    @Value("${BUT_WANT_TAKE_PET}")
    private String BUT_WANT_TAKE_PET;
    @Value("${BUT_SEND_REPORT}")
    private String BUT_SEND_REPORT;
    @Value("${BUT_CALL_VOLUNTEER}")
    private String BUT_CALL_VOLUNTEER;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
