package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения пути к конкретному медиа-контенту
 */
@Getter
public enum MessageImage {
    FIRST_TIME_WELCOME_MSG_IMG("/photos/first_time_welcome_msg.jpg"),
    SORRY_WELCOME_MSG_IMG("/photos/sorry_welcome_msg.jpg"),
    BLOCKED_WELCOME_MSG_IMG("/photos/blocked_welcome_msg.jpg"),
    CAT_SHELTERS_MSG_IMG("/photos/cat_shelters_msg.jpg"),
    DOG_SHELTERS_MSG_IMG("/photos/dog_shelters_msg.jpg"),
    SHELTER_DEFAULT_PREVIEW_MSG_IMG("/photos/shelter_default_preview_msg.jpg"),
    TAKING_PET_MSG_IMG("/photos/taking_pet_msg.jpg"),
    CARE_DOG_REC_MSG_IMG("/photos/care_dog_rec_msg.jpg"),
    CARE_CAT_REC_MSG_IMG("/photos/care_cat_rec_msg.jpg"),
    DOWNLOAD_REPORT_MESSEGE_IMG("/photos/download_report_messege.jpg");

    private final String path;

    MessageImage(String path) {
        this.path = path;
    }
}
