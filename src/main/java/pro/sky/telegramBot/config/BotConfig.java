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

    // ПЕРЕМЕННЫЕ С ТЕКСТОМ ДЛЯ ОТВЕТНЫХ СООБЩЕНИЙ
    @Value("${MSG_WELCOME}")
    private String MSG_WELCOME;
    @Value("${MSG_DEFAULT}")
    private String MSG_DEFAULT;
    @Value("${MSG_SHELTER_INTRO}")
    private String MSG_SHELTER_INTRO;

    // ПЕРЕМЕННЫЕ С ТЕКСТОМ НА КНОПКАХ
    @Value("${BUT_WANT_DOG}")
    private String BUT_WANT_DOG;
    @Value("${BUT_WANT_CAT}")
    private String BUT_WANT_CAT;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
