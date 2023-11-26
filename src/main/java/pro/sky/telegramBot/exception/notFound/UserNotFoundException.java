package pro.sky.telegramBot.exception.notFound;

public class UserNotFoundException extends NotFoundException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
