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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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
    void createNewAdoptionRecordTest() {
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

}
