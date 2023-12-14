package pro.sky.telegramBot.controller.constants;

import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.users.User;

public class UserControllerTestConstants {

    public static final User USER = new User();
    public static final Long USER_ID = 1L;
    public static final String USER_NAME = "Name";
    public static final Long USER_CHAT_ID = 999L;
    public static final UserState USER_STATE = UserState.FREE;
    public static final UserState ANOTHER_USER_STATE = UserState.POTENTIAL;
}
