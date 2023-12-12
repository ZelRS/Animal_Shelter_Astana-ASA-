package pro.sky.telegramBot.controller;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegramBot.config.BotConfig;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.executor.MessageExecutor;
import pro.sky.telegramBot.loader.MediaLoader;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.*;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.sender.specificSenders.NotificationSender;
import pro.sky.telegramBot.service.impl.*;
import pro.sky.telegramBot.utils.keyboardUtils.KeyboardCreator;
import pro.sky.telegramBot.utils.keyboardUtils.SpecificKeyboardCreator;
import pro.sky.telegramBot.utils.mediaUtils.MediaMessageCreator;
import pro.sky.telegramBot.utils.mediaUtils.SpecificMediaMessageCreator;
import pro.sky.telegramBot.utils.statistic.ReportDataConverter;
import pro.sky.telegramBot.utils.statistic.ReportSumCalculator;
import pro.sky.telegramBot.utils.statistic.StatisticPreparer;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pro.sky.telegramBot.enums.TrialPeriodState.*;

@WebMvcTest(AdoptionRecordController.class)
class AdoptionRecordControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdoptionRecordRepository adoptionRecordRepository;
    @MockBean
    private ReportRepository reportRepository;
    @MockBean
    private UserInfoRepository userInfoRepository;
    @MockBean
    private PetRepository petRepository;
    @MockBean
    private ShelterRepository shelterRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TelegramBot telegramBot;
    @SpyBean
    private AdoptionRecordServiceImpl adoptionRecordService;
    @MockBean
    private StatisticPreparer statisticPreparer;
    @MockBean
    private ReportDataConverter reportDataConverter;
    @MockBean
    private MessageSender messageSender;
    @MockBean
    private SpecificKeyboardCreator specificKeyboardCreator;
    @MockBean
    private KeyboardCreator keyboardCreator;
    @MockBean
    private ReportSumCalculator reportSumCalculator;
    @MockBean
    private NotificationSender notificationSender;
    @MockBean
    private MessageExecutor messageExecutor;
    @MockBean
    private SpecificMediaMessageCreator specificMediaMessageCreator;
    @MockBean
    private MediaMessageCreator mediaMessageCreator;
    @MockBean
    private MediaLoader mediaLoader;
    @MockBean
    private BotConfig botConfig;
    @MockBean
    private ShelterServiceImpl shelterService;
    @MockBean
    private ReportServiceImpl reportService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private PetServiceImpl petService;

    private AdoptionRecordController adoptionRecordController;

    @Test
    void testAddAdoptionRecord() throws Exception {
        // Подготовка данных для теста
        User user = new User();
        user.setId(1L);
        user.setState(UserState.PROBATION);
        Pet pet = new Pet();
        pet.setId(1L);
        AdoptionRecord savedAdoptionRecord = new AdoptionRecord();
        savedAdoptionRecord.setId(1L);

        // Настройка поведения
        when(userService.getById(1L)).thenReturn(user);
        when(petService.getById(1L)).thenReturn(pet);
        when(adoptionRecordRepository.save(any(AdoptionRecord.class))).thenReturn(savedAdoptionRecord);

        // Выполнение POST-запроса к методу контроллера
        mockMvc.perform(post("/adoption/record")
                        .param("userId", "1")
                        .param("petId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        // Проверка вызова соответствующих методов сервиса
        verify(userService).getById(1L);
        verify(petService).getById(1L);
        verify(adoptionRecordRepository).save(any(AdoptionRecord.class));
    }
    @Test
    void testExtendAdoptionRecord_Success() throws Exception {
        // Подготовка данных для теста
        Long adoptionRecordId = 1L;

        // Исходная запись об усыновлении
        AdoptionRecord originalAdoptionRecord = new AdoptionRecord();
        originalAdoptionRecord.setId(adoptionRecordId);
        originalAdoptionRecord.setState(PROBATION_EXTEND);

        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setAdoptionRecord(originalAdoptionRecord);

        Pet originalPet = new Pet();
        originalPet.setId(1L);
        originalPet.setAdoptionRecord(originalAdoptionRecord);

        originalAdoptionRecord.setUser(originalUser);
        originalAdoptionRecord.setPet(originalPet);

        // Новая запись, которую сервис должен создать
        AdoptionRecord newAdoptionRecord = new AdoptionRecord();
        newAdoptionRecord.setState(PROBATION_EXTEND);

        // Мокирование вызовов методов
        when(adoptionRecordRepository.findById(adoptionRecordId)).thenReturn(Optional.of(originalAdoptionRecord));
        when(userService.getById(anyLong())).thenReturn(originalUser);
        when(petService.getById(anyLong())).thenReturn(originalPet);
        when(adoptionRecordRepository.save(any(AdoptionRecord.class))).thenReturn(newAdoptionRecord);

        // Вызываем метод контроллера
        mockMvc.perform(put("/adoption/record/{id}", adoptionRecordId)
                        .param("adoptionRecordId", adoptionRecordId.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("PROBATION_EXTEND"));

        // Проверка, что все моки были вызваны с ожидаемыми параметрами
        verify(adoptionRecordRepository).findById(adoptionRecordId);
        verify(adoptionRecordRepository, times(2)).save(any(AdoptionRecord.class)); // Проверяем, был ли сохранен дважды
        verify(userService).update(any(User.class));
        verify(petService).update(any(Pet.class));

        // Проверяем, что статус исходной записи был изменен на CLOSED
        assertThat(originalAdoptionRecord.getState()).isEqualTo(CLOSED);
    }

}