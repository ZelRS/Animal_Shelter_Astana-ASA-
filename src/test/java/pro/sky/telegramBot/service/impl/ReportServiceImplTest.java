package pro.sky.telegramBot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegramBot.loader.MediaLoader;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.repository.ReportRepository;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.statistic.ReportDataConverter;
import pro.sky.telegramBot.utils.statistic.ReportSumCalculator;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private ReportDataConverter reportDataConverter;
    @Mock
    private UserService userService;
    @Mock
    private AdoptionRecordService adoptionRecordService;
    @Mock
    private MessageSender messageSender;
    @Mock
    private ReportSumCalculator reportSumCalculator;
    @Mock
    private TelegramBot bot;
    @Mock
    private MediaLoader mediaLoader;

    @Captor
    private ArgumentCaptor<Report> reportCaptor;

    private ReportServiceImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new ReportServiceImpl(reportRepository, reportDataConverter, userService, adoptionRecordService,
                messageSender, reportSumCalculator, bot, mediaLoader);
    }

    @Test
    void saveReportShouldSaveReportIfNew() {
        Report newReport = new Report(); // настроить свойства
        Long chatId = 123L;
        when(reportRepository.findByReportDateTime(any(LocalDate.class))).thenReturn(null);
        when(reportRepository.save(any(Report.class))).thenReturn(newReport);

        boolean result = underTest.saveReport(newReport, chatId);

        verify(reportRepository).save(reportCaptor.capture());
        Report captured = reportCaptor.getValue();
        assertEquals(newReport, captured);
        assertTrue(result);
    }

}
