package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.users.User;

import java.util.List;
import java.util.Optional;

/**
 * репозиторий для работы с таблицей person базы данных
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

    @Query(value = "select pi2.phone_number from person p join person_info pi2 on p.info_id = pi2.id where p.id = :userId", nativeQuery = true)
    Optional<String> findPhoneById(Long userId);

    List<User> findAllByAdoptionRecordIsNullAndState(UserState state);

    List<User> findAllByState(UserState state);
}
