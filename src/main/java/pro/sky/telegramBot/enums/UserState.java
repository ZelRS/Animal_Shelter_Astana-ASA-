package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum UserState {
    IDLE("новый пользователь"),
    POTENTIAL("потенциальный хозяин"),
    PROBATION("хозяин животного на испытательном сроке"),
    TRUSTED("надежный хозяин животного"),
    UNTRUSTED("ненадежный хозяин животного"),
    BLOCKED("пользователь заблокирован");

    private final String userState;

    UserState(String userState) {
        this.userState = userState;
    }


}
