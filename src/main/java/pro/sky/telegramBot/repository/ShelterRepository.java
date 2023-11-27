package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.shelter.Shelter;

import java.util.List;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    @Query("SELECT s.name FROM shelter s WHERE s.type = :type AND s.name IS NOT NULL")
    List<String> findAllShelterNamesByType(@Param("type") PetType type);

    @Query("SELECT s.description FROM shelter s WHERE s.name=:name")
    String findDescriptionBy(String name);
}
