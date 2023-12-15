package pro.sky.telegramBot.service;

import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.users.User;
import pro.sky.telegramBot.model.users.UserInfo;

import java.util.List;
import java.util.Optional;


/**
 * интерфейс сервиса для обработки запросов к БД пользователей и информации о пользователях
 */
//  предполагается, что данным интерфейсом будут обрабатываться оба репозитория - User и UserInfo
public interface UserService {
    /**
     * получить пользователя из БД по chatId
     */
    User findUserByChatId(Long chatId);

    /**
     * получить пользователя из БД по id
     */
    User getById(Long id);

    /**
     * создать и сохранить пользователя в БД
     */
    User create(User user);

    /**
     * изменить пользователя в БД
     */
    User update(User user);

    /**
     * создать и сохранить информацию о пользователе пользователя в БД
     */
    UserInfo create(UserInfo userInfo);

    Optional<String> getUserPhone(Long id);

    UserInfo setUserPhone(UserInfo userInfo);

    List<User> findAllByAdoptionRecordIsNullAndState(UserState state);

    List<User> findAllByState(UserState userState);

    Long getRandomVolunteerId();

    void addPhoneNumberToPersonInfo(String firstName, String lastName, Long chatId, String phone);

    void setUserState(Long id, UserState state);
}
