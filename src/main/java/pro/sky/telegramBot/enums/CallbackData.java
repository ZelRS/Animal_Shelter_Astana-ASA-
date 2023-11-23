package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum CallbackData {
    DOGS_BUT("dogs_but"),
    CATS_BUT("cats_but");

   private final String callbackData;

    CallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
}
