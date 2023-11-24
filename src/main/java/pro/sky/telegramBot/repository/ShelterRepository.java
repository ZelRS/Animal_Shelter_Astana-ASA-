package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.shelter.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
