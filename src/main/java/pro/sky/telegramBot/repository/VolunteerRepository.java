package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.enums.VolunteerState;
import pro.sky.telegramBot.model.volunteer.Volunteer;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    /**
     * найти всех волонтеров с конкретным статусом
     */
    List<Volunteer> findAllByState(VolunteerState volunteerState);
}
