package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.shelter.Shelter;

import java.util.List;

/**
 * репозиторий для работы с таблицей shelter базы данных
 */
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    @Query("SELECT s.name, COUNT(p) FROM shelter s LEFT JOIN s.pets p GROUP BY s.name")
    List<Object[]> findShelterNamesPetCounts();


    List<Shelter> findByTypeOrderById(PetType type);
}

