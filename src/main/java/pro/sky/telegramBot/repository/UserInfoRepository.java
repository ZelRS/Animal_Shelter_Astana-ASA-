package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.users.UserInfo;

/**
 * репозиторий для работы с таблицей person_info базы данных
 */
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}