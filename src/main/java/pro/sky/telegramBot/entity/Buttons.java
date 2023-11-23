package pro.sky.telegramBot.entity;

import lombok.Data;

@Data
public class Buttons {

    private String name;
    private String callbackData;

    public Buttons(String name, String callbackData) {
        this.name = name;
        this.callbackData = callbackData;
    }
}
