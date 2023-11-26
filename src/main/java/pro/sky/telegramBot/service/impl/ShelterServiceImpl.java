package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.repository.ShelterRepository;
import pro.sky.telegramBot.service.ShelterService;
import pro.sky.telegramBot.utils.StringListCreator;

import java.util.List;


// сервис для обработки запросов к БД приютов
@Service
@RequiredArgsConstructor
public class ShelterServiceImpl implements ShelterService {

    private final ShelterRepository shelterRepository;
    @Override
    public String getShelterNames(PetType type) {
        List<String> shelters = shelterRepository.findAllShelterNamesByType(type);
        StringListCreator stringListCreator = new StringListCreator();
        return stringListCreator.createStringList(shelters);
    }
}
