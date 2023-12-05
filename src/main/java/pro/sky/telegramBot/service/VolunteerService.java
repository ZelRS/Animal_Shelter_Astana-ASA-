package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.volunteer.Volunteer;

import java.util.List;

public interface VolunteerService {

    Volunteer get(Long id);

    Volunteer create(Volunteer volunteer);

    Volunteer update(Volunteer volunteer);

    List<Volunteer> findAllVolunteerByChatId(Long chatId);
}