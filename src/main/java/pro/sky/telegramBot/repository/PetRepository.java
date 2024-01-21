package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.pet.Pet;

/**
 * репозиторий для работы с таблицей pet базы данных
 */
public interface PetRepository extends JpaRepository<Pet, Long> {
}