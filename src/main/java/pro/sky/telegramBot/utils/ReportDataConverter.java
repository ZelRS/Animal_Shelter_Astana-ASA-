package pro.sky.telegramBot.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Класс для преобразования стринговых параметров в необходимый формат для отчета
 */
@Component
public class ReportDataConverter {
    public LocalDate convertToData(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    public int convertToInteger(String valueA6) {
        String value = valueA6.replaceAll("\\.\\d+", ""); // Remove decimal part
        return Integer.parseInt(value);
    }
}
