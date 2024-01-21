package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения команд от пользователя
 */
@Getter
public enum Command {
    START("/start"),
    REPORT("/report"),
    INFO_TABLE("/info_table"),
    DETAILS("/details"),
    ADDRESS("/address"),
    SCHEDULE("/schedule"),
    SCHEMA("/schema"),
    SEC_PHONE("/sec_phone"),
    SAFETY("/safety"),
    CALL_ME("/call_me"),
    VOLUNTEER("/volunteer");

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public static Command fromString(String name) {
        for (Command b : Command.values()) {
            if (b.name.equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }

}
