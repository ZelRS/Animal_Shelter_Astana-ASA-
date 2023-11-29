package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.shelter.Shelter;

import java.util.List;

/**
 * репозиторий для работы с таблицей shelter базы данных
 */
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    List<Shelter> findByTypeOrderById(PetType type);
}

