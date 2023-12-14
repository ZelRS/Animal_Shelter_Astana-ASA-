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

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
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
    public void testCreateReportFromExcel_Success() {
        Long chatId = 12345L;
        List<String> values = Arrays.asList("10", "20", "30", "40", "50", "2023-12-14");

        User mockUser = new User();
        mockUser.setAdoptionRecord(new AdoptionRecord());
        when(userService.findUserByChatId(chatId)).thenReturn(mockUser);
        when(reportDataConverter.isDateWithinRange(any(), any(), any())).thenReturn(true);
        when(reportDataConverter.convertToInteger(any())).thenReturn(100);
        when(reportRepository.findByAdoptionRecordIdAndReportDateTime(any(), any())).thenReturn(null);

        boolean result = underTest.createReportFromExcel(chatId, values);

        assertTrue(result);
        verify(userService, never()).update(any(User.class));
        verify(reportRepository).save(any(Report.class));

    }

    @Test
    void createReportFromExcel_Should_ReturnFalse_When_UserHasNoAdoptionRecord_Test() {
        Long chatId = 123L;
        when(userService.findUserByChatId(anyLong())).thenReturn(new User());

        boolean result = underTest.createReportFromExcel(chatId, Arrays.asList("3", "2", "1", "4", "5", "2023-12-14"));

        assertFalse(result);
        verify(reportRepository, never()).save(any(Report.class));
    }

}
