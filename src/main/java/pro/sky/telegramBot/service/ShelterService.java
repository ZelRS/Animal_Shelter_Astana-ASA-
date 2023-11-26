package pro.sky.telegramBot.service;

import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.shelter.Shelter;

// интерфейс сервиса для обработки запросов к БД приютов
public interface ShelterService {
    String getShelterNames(PetType type);

    Shelter getById(Long id);

    Shelter create(Shelter shelter);
}
