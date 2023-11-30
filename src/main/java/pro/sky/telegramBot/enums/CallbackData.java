package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения callBackData кнопок
 */
@Getter
public enum CallbackData {
    BUT_WANT_DOG("dogs_but"),
    BUT_WANT_CAT("cats_but"),

    BUT_SEND_REPORT("send_report_but"),

    BUT_CALL_VOLUNTEER("call_volunteer_but"),
    BUT_GET_FULL_INFO("get_full_button_info_but"),
    BUT_TAKING_PET("taking_pet_but"),
    BUT_CARE_PET_RECOMMENDATIONS("care_pet_recommendations"),
    BUT_START_REGISTRATION("start_registration");


    private final String callbackData;

    CallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
