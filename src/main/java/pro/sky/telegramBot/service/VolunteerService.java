package pro.sky.telegramBot.service;

import pro.sky.telegramBot.enums.VolunteerState;
import pro.sky.telegramBot.model.volunteer.Volunteer;

import java.util.List;
import java.util.Optional;

public interface VolunteerService {

    Volunteer get(Long id);

    Volunteer create(Volunteer volunteer);

    Volunteer update(Volunteer volunteer);

    /**
     * найти всех волонтеров
     */
    List<Volunteer> findAll();

    /**
     * найти всех волонтеров с конкретным статусом
     */
    List<Volunteer> findAllByState(VolunteerState volunteerState);

    /**
     * найти волонтера по chatId
     */
    Optional<Volunteer> findByChatId(Long chatId);
}