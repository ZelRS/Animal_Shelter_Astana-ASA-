package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.pet.Pet;


public interface PetRepository extends JpaRepository<Pet, Long> {
}