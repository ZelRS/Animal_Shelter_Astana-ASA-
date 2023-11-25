package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum ImageNames {

    WELCOME_IMG("/photos/start.jpg"),
    CATS_IMG("/photos/cats.jpg"),
    DOGS_IMG("/photos/dogs.jpg");

    private final String path;

    ImageNames(String path) {
        this.path = path;
    }
}
