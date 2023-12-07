package pro.sky.telegramBot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.enums.VolunteerState;
import pro.sky.telegramBot.exception.notFound.VolunteerNotFoundException;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.volunteer.Volunteer;
import pro.sky.telegramBot.repository.VolunteerRepository;
import pro.sky.telegramBot.sender.MessageSender;
import pro.sky.telegramBot.service.VolunteerService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final MessageSender messageSender;

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

    /**
     * найти волонтера по chatId
     */
    @Override
    public Optional<Volunteer> findByChatId(Long chatId) {
        return volunteerRepository.findByChatId(chatId);
    }

    @Override
    public void sendMissingPetMessageToVolunteer(User user, Long chatId) {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        if(!volunteers.isEmpty()) {
            for(Volunteer volunteer : volunteers){
                messageSender.sendMissingPetMessageToVolunteerPhotoMessage(user.getId(), volunteer.getChatId());
            }
        }
    }
}
