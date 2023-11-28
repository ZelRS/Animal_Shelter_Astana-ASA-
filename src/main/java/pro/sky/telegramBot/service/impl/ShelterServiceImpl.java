package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.exception.notFound.ShelterNotFoundException;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.repository.ShelterRepository;
import pro.sky.telegramBot.service.ShelterService;
import pro.sky.telegramBot.utils.StringListCreator;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
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
        List<String> shelters = shelterRepository.findByTypeOrderById(type).stream()
                .map(Shelter::getName)
                .collect(Collectors.toList());
        StringListCreator stringListCreator = new StringListCreator();
        return stringListCreator.createStringList(shelters, type);
    }

    @Override
    public List<Shelter> findAllShelterNamesByType(PetType type) {
        return shelterRepository.findByTypeOrderById(type);
    }

    @Override
    public Shelter getById(Long id) {
        return shelterRepository.findById(id).orElseThrow(() -> new ShelterNotFoundException("Приют не найден"));
    }

    @Override
    public void uploadPhoto(Long id, MultipartFile multipartFile) throws IOException {
        log.info("Was invoked method for upload photo to shelter with ID = {}", id);
        Shelter shelter = getById(id);
        shelter.setData(multipartFile.getBytes());
        shelterRepository.save(shelter);
        log.debug("The avatar was uploaded successfully");
    }
}
