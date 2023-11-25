package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegramBot.enums.ShelterTypes;
import pro.sky.telegramBot.model.shelter.Shelter;

import java.util.List;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    @Query("SELECT s.name FROM приют s WHERE s.type = :type AND s.name IS NOT NULL")
    List<String> findAllShelterNamesByType(@Param("type") ShelterTypes type);
}
