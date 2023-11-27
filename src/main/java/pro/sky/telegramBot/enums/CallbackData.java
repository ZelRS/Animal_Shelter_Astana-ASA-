package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum CallbackData {
    BUT_WANT_DOG("dogs_but"),
    BUT_WANT_CAT("cats_but"),

    BUT_WANT_TAKE_PET("want_take_pet_but"),

    BUT_SEND_REPORT("send_report_but"),

    BUT_CALL_VOLUNTEER("call_volunteer_but"),
    BUT_GET_FULL_INFO("get_full_button_info_but");



   private final String callbackData;

    CallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
