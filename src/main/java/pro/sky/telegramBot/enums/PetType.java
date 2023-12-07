package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения типа животного
 */
@Getter
public enum PetType {
    DOG("собака", "собак"),
    CAT("кошка", "кошек"),
    NOPET("нет животного", "нет животных");

    private final String type;
    private final String accusative;

    PetType(String type, String accusative) {
        this.type = type;
        this.accusative = accusative;
    }
}