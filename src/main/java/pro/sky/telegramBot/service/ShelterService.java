package pro.sky.telegramBot.service;

import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.shelter.Shelter;

import java.util.List;

// интерфейс сервиса для обработки запросов к БД приютов
public interface ShelterService {
    Shelter create(Shelter shelter);
    Shelter update(Shelter shelter);
    String getStringOfShelterNames(PetType type);

    List<String> findAllShelterNamesByType(PetType type);

    Shelter getById(Long id);
}
