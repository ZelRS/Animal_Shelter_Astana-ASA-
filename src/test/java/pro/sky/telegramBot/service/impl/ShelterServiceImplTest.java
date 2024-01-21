package pro.sky.telegramBot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.exception.notFound.ShelterNotFoundException;
import pro.sky.telegramBot.service.loaders.MediaLoader;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.repository.ShelterRepository;
import pro.sky.telegramBot.service.servicesForInteractingWithRepositories.impl.ShelterServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelterServiceImplTest {

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private MediaLoader mediaLoader;

    @InjectMocks
    private ShelterServiceImpl shelterService;

    private Shelter testShelter;
    private final Long testShelterId = 1L;

    @BeforeEach
    public void setUp() {
        testShelter = new Shelter();
        testShelter.setId(testShelterId);
        testShelter.setName("Test Shelter");
        testShelter.setType(PetType.DOG);
    }

    @Test
    public void should_Create_Shelter_Successfully_Test() {
        when(shelterRepository.save(any(Shelter.class))).thenReturn(testShelter);

        Shelter savedShelter = shelterService.create(testShelter);

        assertNotNull(savedShelter);
        assertEquals(testShelterId, savedShelter.getId());
        verify(shelterRepository).save(testShelter);
    }

    @Test
    public void should_Update_Shelter_Successfully_Test() {
        when(shelterRepository.save(any(Shelter.class))).thenReturn(testShelter);

        Shelter updatedShelter = shelterService.update(testShelter);

        assertNotNull(updatedShelter);
        assertEquals(testShelterId, updatedShelter.getId());
        verify(shelterRepository).save(testShelter);
    }

    @Test
    public void should_GetById_Shelter_Successfully_Test() {
        when(shelterRepository.findById(testShelterId)).thenReturn(Optional.of(testShelter));

        Shelter foundShelter = shelterService.getById(testShelterId);

        assertNotNull(foundShelter);
        assertEquals(testShelterId, foundShelter.getId());
        verify(shelterRepository).findById(testShelterId);
    }

    @Test
    public void should_Throw_Shelter_NotFound_Exception_Test() {
        when(shelterRepository.findById(testShelterId)).thenReturn(Optional.empty());

        assertThrows(ShelterNotFoundException.class, () -> shelterService.getById(testShelterId));

        verify(shelterRepository).findById(testShelterId);
    }
    @Test
    public void should_Return_A_List_Of_Shelter_Names_By_Type_Test() {
        when(shelterRepository.findByTypeOrderById(PetType.DOG))
                .thenReturn(List.of(testShelter));

        List<Shelter> shelters = shelterService.findAllShelterNamesByType(PetType.DOG);

        assertNotNull(shelters);
        assertEquals(1, shelters.size());
        assertEquals(testShelter, shelters.get(0));
        verify(shelterRepository).findByTypeOrderById(PetType.DOG);
    }


}
