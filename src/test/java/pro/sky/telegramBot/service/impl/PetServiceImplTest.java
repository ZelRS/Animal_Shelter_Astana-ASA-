package pro.sky.telegramBot.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.exception.notFound.PetNotFoundException;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.repository.PetRepository;
import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Test
    public void createShouldReturnSavedPet() {
        Pet expectedPet = new Pet();
        when(petRepository.save(any(Pet.class))).thenReturn(expectedPet);

        Pet actualPet = petService.create(new Pet());

        assertEquals(expectedPet, actualPet);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    public void updateShouldReturnUpdatedPet() {
        Pet expectedPet = new Pet();
        when(petRepository.save(any(Pet.class))).thenReturn(expectedPet);

        Pet actualPet = petService.update(new Pet());

        assertEquals(expectedPet, actualPet);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    public void getByIdShouldReturnPetIfFound() {
        Pet expectedPet = new Pet();
        Long petId = 1L;
        when(petRepository.findById(petId)).thenReturn(Optional.of(expectedPet));

        Pet actualPet = petService.getById(petId);

        assertEquals(expectedPet, actualPet);
        verify(petRepository).findById(petId);
    }

    @Test
    public void getByIdShouldThrowPetNotFoundExceptionIfNotFound() {
        Long petId = 1L;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.getById(petId));
        verify(petRepository).findById(petId);
    }

    @Test
    public void uploadPhotoShouldSavePetWithPhoto() throws IOException {
        Long petId = 1L;
        byte[] photoData = new byte[]{1, 2, 3};
        MultipartFile mockMultipartFile = mock(MultipartFile.class);

        when(mockMultipartFile.getBytes()).thenReturn(photoData);

        Pet pet = new Pet();
        pet.setId(petId);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);

        petService.uploadPhoto(petId, mockMultipartFile);

        verify(petRepository).save(petArgumentCaptor.capture());
        assertArrayEquals(photoData, petArgumentCaptor.getValue().getData());
    }
}
