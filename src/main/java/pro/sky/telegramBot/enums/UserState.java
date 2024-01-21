package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для определения статуса пользователя
 */
@Getter
public enum UserState {
    FREE("свободный"),
    POTENTIAL("потенциальный"),
    PROBATION("на испытательном сроке"),
    PROBATION_REPORT("заполнение отчета"),
    PROBATION_PHOTO("отправление фото"),
    BLOCKED("в черном списке"),
    VOLUNTEER("волонтер"),
    INVITED("приглашен в приют"),
    UNTRUSTED("не надежный");

    private final String state;

    UserState(String state) {
        this.state = state;
    }
}
