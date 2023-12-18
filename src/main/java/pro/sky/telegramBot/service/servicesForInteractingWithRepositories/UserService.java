package pro.sky.telegramBot.service.servicesForInteractingWithRepositories;

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
    User createUserInfo(User user);

    /**
     * изменить пользователя в БД
     */
    User update(User user);

    /**
     * создать и сохранить информацию о пользователе пользователя в БД
     */
    UserInfo createUserInfo(UserInfo userInfo);

    /**
     * Метод позволяет получить номер телефона пользователя
     */
    Optional<String> getUserPhone(Long id);

    /**
     * Метод позволяет получить список пользователей с выбранным статусом,
     * у которых отсутствует запись об усыновлении
     */
    List<User> findAllByAdoptionRecordIsNullAndState(UserState state);

    /**
     * Метод позволяет получить список пользователей с выбранным статусом
     */
    List<User> findAllByState(UserState userState);

    /**
     * метод возвращает chatID случайного волонтера
     */
    Long getRandomVolunteerId();

    /**
     * Метод добавляет номер телефона в таблицу UserInfo
     */
    void addPhoneNumberToPersonInfo(String firstName, String lastName, Long chatId, String phone);

    /**
     * Метод позволяет сменить статус пользователя
     */
    void setUserState(Long id, UserState state);

    /**
     * Метод позволяет получить список пользователей со статусом FREE
     */
    List<User> getFREESateUser();

    /**
     * Метод позволяет получить статус пользователя.
     */
    UserState getUserState(Long chatId, String firstName);
}
