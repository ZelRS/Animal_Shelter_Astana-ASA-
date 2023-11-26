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
    @Value("${telegram.bot.token}")
    private String token;


// предлагается разбить на разные конфиг классы поля, хранящие в себе строки для различных целей

    @Value("${REF_MSG_BACK}")
    private String back_mes;



    @Value("${MSG_WELCOME}")
    private String WELCOME_MES;
    @Value("${MSG_DEFAULT}")
    private String DEFAULT_MES;
    @Value("${MSG_SHELTER_INTRO}")
    private String SHELTER_INTRO_MES;




    @Value("${BUT_WANT_DOG}")
    private String dogs_but;

    @Value("${BUT_WANT_CAT}")
    private String cats_but;





    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        return bot;
    }
}
