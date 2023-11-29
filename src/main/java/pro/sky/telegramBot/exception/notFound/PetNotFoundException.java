package pro.sky.telegramBot.exception.notFound;

/**
 * исключение для выбрасывания в случае, когда животное в таблице не было найдено
 */
public class PetNotFoundException extends NotFoundException {
    public PetNotFoundException(String message) {
        super(message);
    }
}
