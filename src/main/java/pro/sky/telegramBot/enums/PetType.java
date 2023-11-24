package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum PetType {
    DOG("собака"),
    CAT("кошка");

    private final String type;

    PetType(String type) {
        this.type = type;
    }
}
