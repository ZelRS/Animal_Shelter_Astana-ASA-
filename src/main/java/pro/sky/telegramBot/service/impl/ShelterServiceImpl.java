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

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * сервис для обработки запросов к БД приютов
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShelterServiceImpl implements ShelterService {
    private final ShelterRepository shelterRepository;

    /**
     * создать и сохранить приют в БД
     */
    @Override
    public Shelter create(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    /**
     * изменить приют в БД
     */
    @Override
    public Shelter update(Shelter shelter) {
        return shelterRepository.save(shelter);
    }

    /**
     * получить список названий приютов из БД
     */
    @Override
    public String getStringOfShelterNames(PetType type) {
        List<String> shelters = shelterRepository.findByTypeOrderById(type).stream()
                .map(Shelter::getName)
                .collect(Collectors.toList());
        StringListCreator stringListCreator = new StringListCreator();
        return stringListCreator.createStringList(shelters, type);
    }

    /**
     * получить список всех названий приютов из БД по их типу
     */
    @Override
    public List<Shelter> findAllShelterNamesByType(PetType type) {
        return shelterRepository.findByTypeOrderById(type);
    }

    /**
     * получить приют из БД по id
     */
    @Override
    public Shelter getById(Long id) {
        return shelterRepository.findById(id).orElseThrow(() -> new ShelterNotFoundException("Приют не найден"));
    }

    /**
     * загрузить в БД фото приюта
     */
    @Override
    public void uploadPhoto(Long id, MultipartFile multipartFile) throws IOException {
        log.info("Was invoked method for upload photo to shelter with ID = {}", id);
        Shelter shelter = getById(id);
        shelter.setData(multipartFile.getBytes());
        shelterRepository.save(shelter);
        log.debug("The avatar was uploaded successfully");
    }

    /**
     * метод для загрузки схемы проезда в базу
     */
    @Override
    public boolean uploadSchema(Long id, MultipartFile schema) throws Exception {
        Optional<Shelter> shelter = shelterRepository.findById(id);
        if (shelter.isPresent()) {
            shelter.get().setSchema(resizeImage(schema));
        } else {
            return false;
        }
        shelterRepository.save(shelter.get());
        return true;
    }

    /**
     * метод для масштабирования картинки
     */
    private byte[] resizeImage(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
             ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bufferedInputStream);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage resizedImage = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = resizedImage.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(resizedImage, "jpg", arrayOutputStream);
            return arrayOutputStream.toByteArray();
        }
    }
}
