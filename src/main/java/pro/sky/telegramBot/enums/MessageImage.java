package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum MessageImage {
    FIRST_TIME_WELCOME_MSG_IMG("/photos/first_time_welcome_msg.jpg"),
    SORRY_WELCOME_MSG_IMG("/photos/sorry_welcome_msg.jpg"),
    BLOCKED_WELCOME_MSG_IMG("/photos/blocked_welcome_msg.jpg"),
    CAT_SHELTERS_MSG_IMG("/photos/cat_shelters_msg.jpg"),
    DOG_SHELTERS_MSG_IMG("/photos/dog_shelters_msg.jpg"),
    SHELTER_DEFAULT_PREVIEW_MSG_IMG("/photos/shelter_default_preview_msg.jpg");

    private final String path;

    MessageImage(String path) {
        this.path = path;
    }
}
