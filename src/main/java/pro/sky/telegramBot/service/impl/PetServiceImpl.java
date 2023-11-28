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

// сервис для обработки запросов к БД животных
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    @Override
    public Pet create(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public void uploadPhoto(Long id, MultipartFile multipartFile) throws IOException {
        log.info("Was invoked method for upload photo to shelter with ID = {}", id);
        Pet pet = getById(id);
        pet.setData(multipartFile.getBytes());
        petRepository.save(pet);
        log.debug("The avatar was uploaded successfully");
    }

    @Override
    public Pet getById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Животное не найдено"));
    }
}
