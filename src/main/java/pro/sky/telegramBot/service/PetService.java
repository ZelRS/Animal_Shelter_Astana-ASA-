package pro.sky.telegramBot.service;

import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.model.pet.Pet;

import java.io.IOException;

// интерфейс сервиса для обработки запросов к БД животных
public interface PetService {
    Pet getById(Long id);

    Pet create(Pet pet);

    void uploadPhoto(Long id, MultipartFile multipartFile) throws IOException;
}
