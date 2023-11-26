package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.pet.Pet;

// интерфейс сервиса для обработки запросов к БД животных
public interface PetService {
    Pet getById(Long id);

    Pet create(Pet pet);
}
