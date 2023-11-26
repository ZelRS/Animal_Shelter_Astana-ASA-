package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.exception.notFound.PetNotFoundException;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.repository.PetRepository;
import pro.sky.telegramBot.service.PetService;

// сервис для обработки запросов к БД животных
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    @Override
    public Pet create(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Pet getById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Животное не найдено"));
    }
}
