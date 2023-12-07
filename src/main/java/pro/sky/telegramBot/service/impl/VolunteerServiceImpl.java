package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.VolunteerState;
import pro.sky.telegramBot.exception.notFound.VolunteerNotFoundException;
import pro.sky.telegramBot.model.volunteer.Volunteer;
import pro.sky.telegramBot.repository.VolunteerRepository;
import pro.sky.telegramBot.service.VolunteerService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;

    @Override
    public Volunteer get(Long id) {
        return volunteerRepository.findById(id).orElseThrow(() -> new VolunteerNotFoundException("Волонтёр не найден"));
    }

    @Override
    public Volunteer create(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Override
    public Volunteer update(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    /**
     * найти всех волонтеров
     */
    @Override
    public List<Volunteer> findAll() {
        return volunteerRepository.findAll();
    }

    /**
     * найти всех волонтеров с конкретным статусом
     */
    @Override
    public List<Volunteer> findAllByState(VolunteerState volunteerState) {
        return volunteerRepository.findAllByState(volunteerState);
    }
}
