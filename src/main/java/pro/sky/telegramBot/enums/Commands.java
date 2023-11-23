package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum Commands {
    START("/start");

    private final String name;

    Commands(String name) {
        this.name = name;
    }

    public static Commands fromString(String name) {
        for (Commands b : Commands.values()) {
            if (b.name.equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }

}
