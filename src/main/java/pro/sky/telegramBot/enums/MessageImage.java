package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum MessageImage {

    WELCOME_MSG_IMG("/photos/start.jpg"),
    CAT_SHELTERS_MSG_IMG("/photos/cats.jpg"),
    DOG_SHELTERS_MSG_IMG("/photos/dogs.jpg");

    private final String path;

    MessageImage(String path) {
        this.path = path;
    }
}
