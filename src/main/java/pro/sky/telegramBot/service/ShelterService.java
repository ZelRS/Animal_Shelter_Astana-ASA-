package pro.sky.telegramBot.service;

import pro.sky.telegramBot.enums.PetType;

// интерфейс сервиса для обработки запросов к БД приютов
public interface ShelterService {
    String getShelterNames(PetType type);
}
