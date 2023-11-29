package pro.sky.telegramBot.service;

import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.model.pet.Pet;

import java.io.IOException;

/**
 * интерфейс сервиса для обработки запросов к БД животных
 */
public interface PetService {
    /**
     * получить животное из БД по id
     */
    Pet getById(Long id);

    /**
     * создать и сохранить животное в БД
     */
    Pet create(Pet pet);

    /**
     * загрузить в БД фото животного
     */
    void uploadPhoto(Long id, MultipartFile multipartFile) throws IOException;
}
