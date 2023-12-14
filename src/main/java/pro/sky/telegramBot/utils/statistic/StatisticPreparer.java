package pro.sky.telegramBot.utils.statistic;

import org.springframework.stereotype.Component;
import pro.sky.telegramBot.model.Adoption.AdoptionRecord;
import pro.sky.telegramBot.model.Adoption.Report;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class StatisticPreparer {
    public int checkProgress(AdoptionRecord adoptionRecord, List<Report> reports) {
        LocalDate adoptionDate = adoptionRecord.getAdoptionDate();
        if (!validateReportValues(adoptionRecord, reports)){
            return -1;
        }
        LocalDate currentDate = LocalDate.now();
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

    public boolean validateReportValues(AdoptionRecord adoptionRecord, List<Report> reports) {
        for(Report report : reports){
            if (!report.getReportDateTime().isBefore(adoptionRecord.getAdoptionDate()) &&
                report.getReportDateTime().isAfter(adoptionRecord.getTrialPeriodEnd())){
                return false;
            }
            if (report.getDietAppetite() < 0 || report.getDietAppetite() > 10 ||
                report.getDietAllergies() < 0 || report.getDietAllergies() > 10 ||
                report.getBehaviorChange() < 0 || report.getBehaviorChange() > 10 ||
                report.getDietPreferences() < 0 || report.getDietPreferences() > 10 ||
                report.getHealthStatus() < 0 || report.getHealthStatus() > 10) {
                return false;
            }
        }
        return true;
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
