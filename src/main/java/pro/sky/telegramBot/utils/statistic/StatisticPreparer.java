package pro.sky.telegramBot.utils.statistic;

import org.springframework.stereotype.Component;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class StatisticPreparer {
    public int checkProgress(AdoptionRecord adoptionRecord, List<Report> reports) {
        LocalDate currentDate = LocalDate.now();
        LocalDate adoptionDate = adoptionRecord.getAdoptionDate();
        int reportCountShould = (int) ChronoUnit.DAYS.between(adoptionDate, currentDate);
        int reportCountFact = reports.size();
        int reportCountDifference = reportCountShould - reportCountFact;
        int photoCountDifference = reportCountShould;
        int ratingTotal = 0;
        for (Report report : reports) {
            ratingTotal =+ report.getRatingTotal();
            if (report.getData().length > 0){
                photoCountDifference --;
            }
        }

        int overallScore = ratingTotal - reportCountDifference*200 - photoCountDifference*50;
        return makeDecision(reportCountShould, overallScore);

    }

    private int makeDecision(int reportCountShould, int overallScore) {
        int maxScore = reportCountShould * 600;
        double percentage = (double) overallScore / maxScore * 100;
        if (percentage < 60) {
            return -1;
        } else if (percentage >= 60 && percentage < 80) {
            return 0;
        } else {
           return 1;
        }
    }
}
