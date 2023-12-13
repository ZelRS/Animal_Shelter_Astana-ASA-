package pro.sky.telegramBot.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.exception.notFound.ShelterNotFoundException;
import pro.sky.telegramBot.loader.MediaLoader;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.repository.ShelterRepository;

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
    public void shouldCreateShelterSuccessfully() {
        when(shelterRepository.save(any(Shelter.class))).thenReturn(testShelter);

        Shelter savedShelter = shelterService.create(testShelter);

        assertNotNull(savedShelter);
        assertEquals(testShelterId, savedShelter.getId());
        verify(shelterRepository).save(testShelter);
    }

    @Test
    public void shouldUpdateShelterSuccessfully() {
        when(shelterRepository.save(any(Shelter.class))).thenReturn(testShelter);

        Shelter updatedShelter = shelterService.update(testShelter);

        assertNotNull(updatedShelter);
        assertEquals(testShelterId, updatedShelter.getId());
        verify(shelterRepository).save(testShelter);
    }

    @Test
    public void shouldGetByIdShelterSuccessfully() {
        when(shelterRepository.findById(testShelterId)).thenReturn(Optional.of(testShelter));

        Shelter foundShelter = shelterService.getById(testShelterId);

        assertNotNull(foundShelter);
        assertEquals(testShelterId, foundShelter.getId());
        verify(shelterRepository).findById(testShelterId);
    }

    @Test
    public void shouldThrowShelterNotFoundException() {
        when(shelterRepository.findById(testShelterId)).thenReturn(Optional.empty());

        assertThrows(ShelterNotFoundException.class, () -> shelterService.getById(testShelterId));

        verify(shelterRepository).findById(testShelterId);
    }


}
