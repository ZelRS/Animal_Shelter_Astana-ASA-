package pro.sky.telegramBot.utils.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.sender.specificSenders.NotificationSender;

import java.util.List;

import static pro.sky.telegramBot.enums.TrialPeriodState.*;
import static pro.sky.telegramBot.enums.TrialPeriodState.UNSUCCESSFUL;

/**
 * Класс для анализа отчетов
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReportAnalyser {
    private final StatisticPreparer statisticPreparer;
    private final NotificationSender notificationSender;

    /**
     * Метод подготавливает окончательную оценку и
     * инициирует информирование волонтера и пользователя о результатах
     */
    public int analyzeReportsResults(AdoptionRecord adoptionRecord, List<Report>reports, Long userChatId) {

        int overallScore = statisticPreparer.checkProgress(adoptionRecord, reports);//получаем оценку результатов отчетов
        if (overallScore == 0){
            if(adoptionRecord.getTrialPeriodDays() == 30) {
                adoptionRecord.setState(PROBATION_EXTEND);
            } else {
                adoptionRecord.setState(UNSUCCESSFUL);
            }
        }else if (overallScore == 1){
            adoptionRecord.setState(SUCCESSFUL);
        }else if (overallScore == -1){
            adoptionRecord.setState(UNSUCCESSFUL);
        }
        return overallScore;

    }
    /**
     * Метод оценивает результаты обработки отчетов, чтобы сформировать соответствующее сообщение
     */
    @NotNull
    public static String getNotificationAction(int overallScore) {
        String notificationAction;
        if (overallScore == -1) {
            notificationAction = "problem"; //общий бал меньше допустимого, животное передавать нельзя
        } else if (overallScore == 0) {
            notificationAction = "try your best"; //средний успех, можно продлить испытательный период
        } else if (overallScore == 1) {
            notificationAction = "good job";//все хорошо, можно передать животное
        } else {
            notificationAction = "calculations error";//в процессе подсчета получены неожиданные результаты
        }
        return notificationAction;
    }
}
