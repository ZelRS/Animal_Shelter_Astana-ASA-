package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.adoption.Report;

import java.time.LocalDate;


public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findByAdoptionRecordIdAndReportDateTime(Long id, LocalDate date);

    Report findByReportDateTime(LocalDate date);

}
