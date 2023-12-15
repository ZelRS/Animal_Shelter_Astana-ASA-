package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.users.User;

import java.time.LocalDate;
import java.util.List;

public interface AdoptionRecordRepository extends JpaRepository<AdoptionRecord, Long> {
    List<Report> findReportsByUser(User user);

    @Query("SELECT DISTINCT ar.user FROM adoption_record ar " +
            "LEFT JOIN ar.reports r " +
            "WHERE ar.state = pro.sky.telegramBot.model.adoption.AdoptionRecord.TrialPeriodState.PROBATION " +
            "AND (r.reportDateTime IS NULL OR r.reportDateTime < CURRENT_DATE)")
    List<User> findUsersWithProbationAndNoReportToday();

    @Query("SELECT DISTINCT ar.user FROM adoption_record ar " +
            "JOIN ar.reports r " +
            "WHERE r.reportDateTime = CURRENT_DATE " +
            "AND (r.data IS NULL OR r.data = '')")
    List<User> findUsersWithReportTodayAndNoPhoto();

    List<AdoptionRecord> findByTrialPeriodEndAfterAndState(LocalDate currentDate, AdoptionRecord.TrialPeriodState state);

    @Query("SELECT r FROM report r WHERE r.adoptionRecord = :adoptionRecord")
    List<Report> findAllReportsByAdoptionRecord(@Param("adoptionRecord") AdoptionRecord adoptionRecord);

}
