package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.exception.notFound.ShelterNotFoundException;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.repository.ShelterRepository;
import pro.sky.telegramBot.service.ShelterService;
import pro.sky.telegramBot.utils.StringListCreator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterServiceImpl implements ShelterService {
    private final ShelterRepository shelterRepository;

    @Override
    public Shelter create(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    @Override
    public Shelter update(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    @Override
    public String getStringOfShelterNames(PetType type) {
        List<String> shelters = shelterRepository.findAllShelterNamesByType(type);
        StringListCreator stringListCreator = new StringListCreator();
        return stringListCreator.createStringList(shelters, type);
    }

    @Override
    public List<String> findAllShelterNamesByType(PetType type) {
        return shelterRepository.findAllShelterNamesByType(type);
    }

    @Override
    public Shelter getById(Long id) {
        return shelterRepository.findById(id).orElseThrow(() -> new ShelterNotFoundException("Приют не найден"));
    }

    @Override
    public String getDescriptionBy(String name) {
        return shelterRepository.findDescriptionBy(name);
    }
}
