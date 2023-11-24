package pro.sky.telegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegramBot.model.users.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}