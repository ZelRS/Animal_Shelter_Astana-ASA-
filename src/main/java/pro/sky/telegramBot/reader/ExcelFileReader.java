package pro.sky.telegramBot.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для обработки документов в формате .xlsx
 */

@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class ExcelFileReader {

    /**
     * Метод для извлечения значений из отчета пользователя
     */
        public List<String> getValues(byte[] inputStream) {
            List<String> values = new ArrayList<>();
            try {
                // Получаем рабочую книгу из inputStream
                Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(inputStream));

                // Получаем первый лист документа
                Sheet sheet = workbook.getSheetAt(0);

                // Получаем значения из нужных ячеек
                String valueA6 = getCellValue(sheet, "A6");
                log.info("Get value A6: {}", valueA6);

                String valueA8 = getCellValue(sheet, "A8");
                log.info("Get value A8: {}", valueA8);

                String valueA10 = getCellValue(sheet, "A10");
                log.info("Get value A10: {}", valueA10);

                String valueA12 = getCellValue(sheet, "A12");
                log.info("Get value A12: {}", valueA12);

                String valueA14 = getCellValue(sheet, "A14");
                log.info("Get value A14: {}", valueA14);

                String dateB2 = getCellValue(sheet, "B2");
                log.info("Get value B2: {}", dateB2);

                // Складываем в список полученные значения
                values.add(valueA6);
                values.add(valueA8);
                values.add(valueA10);
                values.add(valueA12);
                values.add(valueA14);
                values.add(dateB2);

                // Закрываем рабочую книгу
                workbook.close();
            } catch (IOException e) {
                log.error("Error reading Excel file", e);
            }
            return values;
        }

    /**
     * Метод для извлечения значений из конкретных ячеек документа
     */

    // Метод получения параметров из выбранных ячеек в виде строковых переменных
    private static String getCellValue(Sheet sheet, String cellReference) {
        log.info("Trying to get cell value for cell reference: {}", cellReference);

        int columnIndex = CellReference.convertColStringToIndex(cellReference.substring(0, 1));
        log.info("Column index: {}", columnIndex);

        int rowIndex = Integer.parseInt(cellReference.substring(1)) - 1;
        log.info("Row index: {}", rowIndex);

        Row row = sheet.getRow(rowIndex);

        if (row != null) {
            Cell cell = row.getCell(columnIndex);

            if (cell != null) {
                return cell.toString();
            } else {
                log.warn("Cell is null for cell reference: {}", cellReference);
            }
        }

        return ""; // возвращаем пустоту
    }
}
