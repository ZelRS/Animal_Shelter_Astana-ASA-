package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum CallbackData {
    BUT_WANT_DOG("dogs_but"),
    BUT_WANT_CAT("cats_but"),

    BUT_WANT_TAKE_PET("want_take_pet_but"),

    BUT_SEND_REPORT("want_take_pet_but"),

    BUT_CALL_VOLUNTEER("want_take_pet_but");



   private final String callbackData;

    CallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
