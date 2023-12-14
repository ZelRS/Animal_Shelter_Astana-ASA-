package pro.sky.telegramBot.utils.statistic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Класс для преобразования стринговых параметров в необходимый формат для отчета
 */
@Component
@Slf4j
public class ReportDataConverter {
    public LocalDate convertToData(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    public int convertToInteger(String valueA6) {
        String value = valueA6.replaceAll("\\.\\d+", ""); // Remove decimal part
        return Integer.parseInt(value);
    }
    public boolean isDateWithinRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
