package pro.sky.telegramBot.service;

import pro.sky.telegramBot.model.volunteer.Volunteer;

public interface VolunteerService {

    Volunteer create(Volunteer volunteer);

    Volunteer update(Volunteer volunteer);

    Volunteer getById(Long id);

    Volunteer getByChatId(Long chatId);
}