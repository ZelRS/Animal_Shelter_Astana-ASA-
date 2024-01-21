package pro.sky.telegramBot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ButtonConfig {
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
}
