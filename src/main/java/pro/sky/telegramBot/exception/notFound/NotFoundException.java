package pro.sky.telegramBot.exception.notFound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * абстрактный класс исключения not found. Его наследники будут<br>
 * выбрасываться в случаях, когда конкретная сущность не была найдена в БД
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public abstract class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
