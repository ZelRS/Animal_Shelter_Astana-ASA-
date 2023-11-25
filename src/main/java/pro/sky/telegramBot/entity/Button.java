package pro.sky.telegramBot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

// класс описывает сущность кнопок
@Data
@AllArgsConstructor
public class Button {
    // текст на кнопке(название кнопки)
    private String name;
    // данные обратного вызова
    private String callbackData;
}
