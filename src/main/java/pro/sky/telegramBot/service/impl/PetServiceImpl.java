package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.exception.notFound.PetNotFoundException;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.repository.PetRepository;
import pro.sky.telegramBot.service.PetService;

import javax.transaction.Transactional;
import java.io.IOException;

/**
 * сервис для обработки запросов к БД животных
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    /**
     * создать и сохранить животное в БД
     */
    public Pet create(Pet pet) {
        if (pet == null) {
            throw new IllegalArgumentException("Pet argument cannot be null");
        }
        return petRepository.save(pet);
    }

    /**
     * загрузить в БД фото животного
     */
    @Override
    public void uploadPhoto(Long id, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("The file is empty");
        }
        Pet pet = getById(id);
        pet.setData(multipartFile.getBytes());
        petRepository.save(pet);
    }

    /**
     * изменить животное в БД
     */
    @Override
    public Pet update(Pet pet) {
        if (pet == null) {
            throw new IllegalArgumentException("Pet cannot be null");
        }
        return petRepository.save(pet);
    }

    /**
     * получить животное из БД по id
     */
    @Override
    public Pet getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        return petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Животное не найдено"));
    }
}
