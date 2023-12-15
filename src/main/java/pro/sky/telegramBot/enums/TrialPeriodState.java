package pro.sky.telegramBot.enums;

import lombok.Getter;

@Getter
public enum TrialPeriodState {

    PROBATION("испытательный срок"),
    PROBATION_EXTEND("продление испытательного срока"),
    SUCCESSFUL("успешно завершен"),
    UNSUCCESSFUL("неуспешно завершен"),
    CLOSED("закрыто");

    private final String state;

    TrialPeriodState(String state) {
        this.state = state;
    }
}
