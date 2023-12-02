package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.model.volunteer.Volunteer;
import pro.sky.telegramBot.service.VolunteerService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VolunteerServiceImpl implements VolunteerService {

    @Override
    public Volunteer create(Volunteer volunteer) {
        return null;
    }

    @Override
    public Volunteer update(Volunteer volunteer) {
        return null;
    }

    @Override
    public Volunteer getById(Long id) {
        return null;
    }

    @Override
    public Volunteer getByChatId(Long chatId) {
        return null;
    }
}
