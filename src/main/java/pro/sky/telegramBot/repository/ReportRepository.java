package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.Adoption.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
