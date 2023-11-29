package pro.sky.telegramBot.exception.notFound;

/**
 * исключение для выбрасывания в случае, когда пользователь в таблице не был найден
 */
public class UserNotFoundException extends NotFoundException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
