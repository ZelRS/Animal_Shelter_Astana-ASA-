package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.repository.ShelterRepository;
import pro.sky.telegramBot.service.ShelterService;
import pro.sky.telegramBot.utils.ListCreator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterServiceImpl implements ShelterService {

    private final ShelterRepository shelterRepository;
    @Override
    public String getShelterNames(PetType type) {
        List<String> shelters = shelterRepository.findAllShelterNamesByType(type);
        ListCreator listCreator = new ListCreator();
        return listCreator.createList(shelters);
    }
}
