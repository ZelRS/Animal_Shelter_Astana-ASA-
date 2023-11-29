package pro.sky.telegramBot.utils.keyboardUtils;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.entity.Button;


import java.util.ArrayList;
import java.util.List;

/**
 * класс содержит метод для создания абстрактной однострочной<br>
 * клавиатуры для вставки под сообщение, отправляемое пользователю
 */
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class InlineKeyboardCreator {

    public InlineKeyboardMarkup createInlineKeyboard(List<Button> buttonNames) {

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Button buttonsName : buttonNames) {
            String callbackData = buttonsName.getCallbackData();
            String buttonName = buttonsName.getName();
            InlineKeyboardButton button = new InlineKeyboardButton(buttonName).callbackData(callbackData);
            buttons.add(button);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(buttons.toArray(new InlineKeyboardButton[0]));

        return inlineKeyboardMarkup;
    }
}