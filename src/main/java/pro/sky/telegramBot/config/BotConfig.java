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

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
