package pro.sky.telegramBot.service;

import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.shelter.Shelter;

import java.io.IOException;
import java.util.List;

// интерфейс сервиса для обработки запросов к БД приютов
public interface ShelterService {
    Shelter create(Shelter shelter);

    Shelter update(Shelter shelter);

    String getStringOfShelterNames(PetType type);

    List<Shelter> findAllShelterNamesByType(PetType type);

    Shelter getById(Long id);

    void uploadPhoto(Long id, MultipartFile multipartFile) throws IOException;
}
