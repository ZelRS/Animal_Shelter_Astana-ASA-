package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegramBot.model.users.User;

import java.util.Optional;

/**
 * репозиторий для работы с таблицей person базы данных
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

    @Query(value = "select pi2.phone_number from person p join person_info pi2 on p.info_id = pi2.id where p.id = :userId", nativeQuery = true)
    Optional<String> findPhoneById(Long userId);
}
