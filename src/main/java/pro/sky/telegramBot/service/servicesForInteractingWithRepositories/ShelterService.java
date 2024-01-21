package pro.sky.telegramBot.service.servicesForInteractingWithRepositories;

import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.shelter.Shelter;

import java.io.IOException;
import java.util.List;

/**
 * интерфейс сервиса для обработки запросов к БД приютов
 */
public interface ShelterService {
    /**
     * создать и сохранить приют в БД
     */
    Shelter create(Shelter shelter);

    /**
     * изменить приют в БД
     */
    Shelter update(Shelter shelter);

    /**
     * получить список названий приютов из БД
     */
    String getStringOfShelterNames(PetType type);

    /**
     * получить список всех названий приютов из БД по их типу
     */
    List<Shelter> findAllShelterNamesByType(PetType type);

    /**
     * получить приют из БД по id
     */
    Shelter getById(Long id);

    /**
     * загрузить в БД фото приюта
     */
    void uploadPhoto(Long id, MultipartFile multipartFile) throws IOException;

    /**
     * загрузить схему проезда в базу
     */
    boolean uploadSchema(Long id, MultipartFile schema) throws Exception;

    /**
     * Метод позволяет получить список имен приютов с количеством животных в нем
     */
    List<String> getShelterNamesWitPetCounts();
}
