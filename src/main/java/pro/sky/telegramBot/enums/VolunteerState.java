package pro.sky.telegramBot.enums;

import lombok.Getter;


/**
 * enum для определения статуса волонтера
 */
@Getter
public enum VolunteerState {
    FREE("свободен"),

    BUSY("занят");

    private final String state;

    VolunteerState(String state) {
        this.state = state;
    }

}
