package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum CallbackData {
    DOG_BUT("dogs_but"),
    CAT_BUT("cats_but");

   private final String callbackData;

    CallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
