package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения команд от пользователя
 */
@Getter
public enum Command {
    START("/start");

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
