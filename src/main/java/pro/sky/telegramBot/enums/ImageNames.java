package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum ImageNames {

    WELCOME_IMG("/photos/start.jpg");

    private final String path;

    ImageNames(String path) {
        this.path = path;
    }
}
