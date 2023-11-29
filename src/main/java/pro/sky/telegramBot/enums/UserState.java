package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения статуса пользователя
 */
@Getter
public enum UserState {
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
