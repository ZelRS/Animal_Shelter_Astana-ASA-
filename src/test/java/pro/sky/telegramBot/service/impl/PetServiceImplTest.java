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
    public void create_Should_Return_SavedPet() {
        Pet expectedPet = new Pet();
        when(petRepository.save(any(Pet.class))).thenReturn(expectedPet);

        Pet actualPet = petService.create(new Pet());

        assertEquals(expectedPet, actualPet);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    public void update_Should_Return_UpdatedPet() {
        Pet expectedPet = new Pet();
        when(petRepository.save(any(Pet.class))).thenReturn(expectedPet);

        Pet actualPet = petService.update(new Pet());

        assertEquals(expectedPet, actualPet);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    public void getById_Should_Return_Pet_IfFound_Test() {
        Pet expectedPet = new Pet();
        Long petId = 1L;
        when(petRepository.findById(petId)).thenReturn(Optional.of(expectedPet));

        Pet actualPet = petService.getById(petId);

        assertEquals(expectedPet, actualPet);
        verify(petRepository).findById(petId);
    }

    @Test
    public void getById_Should_Throw_PetNotFound_Exception_IfNotFound_Test() {
        Long petId = 1L;
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.getById(petId));
        verify(petRepository).findById(petId);
    }

    @Test
    public void uploadPhoto_Should_SavePet_WithPhoto_Test() throws IOException {
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
    @Test
    public void create_Should_ThrowException_When_PetIsNull_Test() {
        assertThrows(IllegalArgumentException.class, () -> petService.create(null));
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void update_Should_ThrowException_When_PetIsNull_Test() {
        assertThrows(IllegalArgumentException.class, () -> petService.update(null));
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    public void uploadPhoto_Should_ThrowException_When_FileIsEmpty_Test() {
        Long petId = 1L;
        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        when(mockMultipartFile.isEmpty()).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> petService.uploadPhoto(petId, mockMultipartFile));

    }


    @Test
    public void uploadPhoto_Should_ThrowException_When_IdIsNull_Test() {
        MultipartFile mockMultipartFile = mock(MultipartFile.class);

        assertThrows(IllegalArgumentException.class, () -> petService.uploadPhoto(null, mockMultipartFile));
        verify(petRepository, never()).findById(anyLong());
    }

    @Test
    public void getById_Should_ThrowException_When_IdIsNull_Test() {
        assertThrows(IllegalArgumentException.class, () -> petService.getById(null));
        verify(petRepository, never()).findById(anyLong());
    }
}
