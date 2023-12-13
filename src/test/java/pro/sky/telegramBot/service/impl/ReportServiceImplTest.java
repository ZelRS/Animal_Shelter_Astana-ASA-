package pro.sky.telegramBot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegramBot.loader.MediaLoader;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.ReportRepository;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.AdoptionRecordService;
import pro.sky.telegramBot.service.UserService;
import pro.sky.telegramBot.utils.statistic.ReportDataConverter;
import pro.sky.telegramBot.utils.statistic.ReportSumCalculator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

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
    void saveReport_Should_SaveReport_IfNew_Test() {
        Report newReport = new Report();
        Long chatId = 123L;
        when(reportRepository.findByReportDateTime(any(LocalDate.class))).thenReturn(null);
        when(reportRepository.save(any(Report.class))).thenReturn(newReport);

        boolean result = underTest.saveReport(newReport, chatId);

        verify(reportRepository).save(reportCaptor.capture());
        Report captured = reportCaptor.getValue();
        assertEquals(newReport, captured);
        assertTrue(result);
    }
    @Test
    void createReportFromExcel_Should_CreateReport_When_ValidInput_Test() {
        Long chatId = 123L;
        User user = new User();
        user.setAdoptionRecord(new AdoptionRecord());
        when(userService.findUserByChatId(anyLong())).thenReturn(user);
        when(reportRepository.findByAdoptionRecordIdAndReportDateTime(anyLong(), any(LocalDate.class))).thenReturn(null);
        when(reportRepository.save(any(Report.class))).thenAnswer(invocation -> {
            Report report = invocation.getArgument(0);
            report.setId(1L);
            return report;
        });
        when(reportRepository.findById(1L)).thenReturn(Optional.of(new Report()));

        boolean result = underTest.createReportFromExcel(chatId, Arrays.asList("3", "2", "1", "4", "5", "2021-12-01"));

        assertTrue(result);
        verify(reportRepository, times(2)).save(any(Report.class));
    }

    @Test
    void createReportFromExcel_Should_ReturnFalse_When_UserHasNoAdoptionRecord_Test() {
        Long chatId = 123L;
        when(userService.findUserByChatId(anyLong())).thenReturn(new User());

        boolean result = underTest.createReportFromExcel(chatId, Arrays.asList("3", "2", "1", "4", "5", "2021-12-01"));

        assertFalse(result);
        verify(reportRepository, never()).save(any(Report.class));
    }

}
