package pro.sky.telegramBot.exception.notFound;

/**
 * исключение для выбрасывания в случае, когда приют в таблице не был найден
 */
public class ShelterNotFoundException extends NotFoundException{
    public ShelterNotFoundException(String message) {
        super(message);
    }
}
