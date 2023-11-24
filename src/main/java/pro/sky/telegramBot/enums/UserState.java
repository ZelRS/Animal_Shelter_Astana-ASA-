package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum UserState {
    // статусы пользователя
    FREE("свободный"),
    POTENTIAL("потенциальный усыновитель"),
    PROBATION("на испытательном сроке"),
    TRUSTED("проверенный"),
    UNTRUSTED("не надежный"),
    BLOCKED("в черном списке");

    private final String state;

    UserState(String state) {
        this.state = state;
    }
}
