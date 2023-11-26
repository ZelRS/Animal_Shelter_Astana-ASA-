package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.users.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);
}
