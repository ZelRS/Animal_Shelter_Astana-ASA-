package pro.sky.telegramBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommandDevelopTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommandDevelopTelegramBotApplication.class, args);
    }

}
