package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.service.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adoption/record/report")
@Tag(name = "API для работы с отчетами")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getReportPhoto(@PathVariable Long id) {
        Report report = reportService.getReportById(id);
        if (report == null || report.getData() == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(report.getData());
        return ResponseEntity.ok()
                .contentLength(report.getData().length)
                .header("Content-Disposition", "attachment; filename=\"pet.jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

}
