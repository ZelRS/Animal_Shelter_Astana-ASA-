package pro.sky.telegramBot.enums;

import lombok.Getter;

/**
 * enum для вопросов отчета
 */
@Getter
public enum QuestionsForReport {
    DIETAPPETITE("Заметили ли Вы изменения аппетита: от 0 = значительное снижение до 10 = хороший аппетит"),
    DIETPREFERENCES("Есть ли изменения в предпочитаемом рационе: от 0 = значительные до 10 = отсутствуют"),
    DIETALLERGIES("Заметили ли Вы аллергические реакции: от 0 = значительные до 10 = никаких"),
    HEALTHSTATUS("Общее состояние здоровья: от 0 = плохое до 10 = отличное"),
    BEHAVIORCHANGE("Заметили ли Вы изменения в поведении: от 0 = значительные изменения до 10 = без изменений");

    private final String question;

    QuestionsForReport(String question) {
        this.question = question;
    }
}
