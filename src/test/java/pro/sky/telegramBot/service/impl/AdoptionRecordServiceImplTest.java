package pro.sky.telegramBot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.repository.AdoptionRecordRepository;
import pro.sky.telegramBot.service.PetService;
import pro.sky.telegramBot.service.UserService;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pro.sky.telegramBot.enums.TrialPeriodState.*;

class AdoptionRecordServiceImplTest {

    @Mock
    private AdoptionRecordRepository adoptionRecordRepository;

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @InjectMocks
    private AdoptionRecordServiceImpl adoptionRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_NewAdoptionRecord_Test() {
        Long userId = 1L;
        Long petId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        Pet mockPet = new Pet();
        mockPet.setId(petId);

        AdoptionRecord expectedRecord = new AdoptionRecord();

        when(userService.getById(anyLong())).thenReturn(mockUser);
        when(petService.getById(anyLong())).thenReturn(mockPet);

        when(adoptionRecordRepository.save(any(AdoptionRecord.class))).thenReturn(expectedRecord);

        AdoptionRecord actualRecord = adoptionRecordService.createNewAdoptionRecord(userId, petId);

        assertNotNull(actualRecord);
        assertEquals(expectedRecord, actualRecord);
        verify(userService).update(mockUser);
        verify(petService).update(mockPet);
    }
    @Test
    void extendAdoptionRecord_whenRecordCanBeExtended() {
        Long recordId = 1L;
        AdoptionRecord existingRecord = new AdoptionRecord();
        existingRecord.setState(PROBATION_EXTEND);

        User mockUser = new User();
        Pet mockPet = new Pet();
        existingRecord.setUser(mockUser);
        existingRecord.setPet(mockPet);

        when(adoptionRecordRepository.findById(recordId)).thenReturn(Optional.of(existingRecord));

        AdoptionRecord extendedRecord = new AdoptionRecord();
        extendedRecord.setState(PROBATION);
        when(adoptionRecordRepository.save(any(AdoptionRecord.class))).thenReturn(extendedRecord);

        AdoptionRecord actualRecord = adoptionRecordService.extendAdoptionRecord(recordId);

        assertNotNull(actualRecord);
        assertEquals(PROBATION, actualRecord.getState());
        verify(adoptionRecordRepository, times(2)).save(any(AdoptionRecord.class));
        assertEquals(CLOSED, existingRecord.getState());
    }


    @Test
    void extendAdoptionRecord_whenRecordCannotBeExtended() {
        Long recordId = 1L;
        when(adoptionRecordRepository.findById(anyLong())).thenReturn(Optional.empty());

        AdoptionRecord result = adoptionRecordService.extendAdoptionRecord(recordId);

        assertNull(result, "Adoption record not found");
    }

    @Test
    void terminateAdoptionRecord_whenRecordExists() {
        Long recordId = 1L;
        AdoptionRecord existingRecord = new AdoptionRecord();
        existingRecord.setState(PROBATION);

        User mockUser = new User();
        Pet mockPet = new Pet();
        existingRecord.setUser(mockUser);
        existingRecord.setPet(mockPet);

        when(adoptionRecordRepository.findById(recordId)).thenReturn(Optional.of(existingRecord));
        when(adoptionRecordRepository.save(any(AdoptionRecord.class))).thenReturn(existingRecord);

        AdoptionRecord actualRecord = adoptionRecordService.terminateAdoptionRecord(recordId);

        assertNotNull(actualRecord);
        assertEquals(CLOSED, actualRecord.getState());
        verify(adoptionRecordRepository).save(existingRecord);
    }

    @Test
    void terminateAdoptionRecord_whenRecordDoesNotExist() {
        Long recordId = 1L;
        when(adoptionRecordRepository.findById(anyLong())).thenReturn(Optional.empty());

        AdoptionRecord result = adoptionRecordService.extendAdoptionRecord(recordId);

        assertNull(result, "Adoption record not found");
    }
}
