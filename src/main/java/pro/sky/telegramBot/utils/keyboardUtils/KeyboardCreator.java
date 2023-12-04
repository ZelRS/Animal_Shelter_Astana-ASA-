package pro.sky.telegramBot.utils.keyboardUtils;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.entity.Button;


import java.util.ArrayList;
import java.util.List;

/**
 * класс содержит методы для создания абстрактных клавиатур<br>
 * клавиатуры в одну строку для вставки под сообщение, отправляемое пользователю
 * клавиатуры в один столбик для вставки под сообщение, отправляемое пользователю
 * клавиатуры для вставки в поле ввода
 */
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class KeyboardCreator {

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
    //Метод создает два ряда по 5 кнопок
    public InlineKeyboardMarkup createInlineKeyboardTwoRow(List<Button> buttonNames) {
        List<InlineKeyboardButton> buttonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow2 = new ArrayList<>();

        for (int i = 0; i < buttonNames.size(); i++) {
            Button button = buttonNames.get(i);
            String callbackData = button.getCallbackData();
            String buttonName = button.getName();
            InlineKeyboardButton inlineButton = new InlineKeyboardButton(buttonName).callbackData(callbackData);

            if (i < buttonNames.size() / 2) {
                buttonsRow1.add(inlineButton);
            } else {
                buttonsRow2.add(inlineButton);
            }
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(buttonsRow1.toArray(new InlineKeyboardButton[0]));
        inlineKeyboardMarkup.addRow(buttonsRow2.toArray(new InlineKeyboardButton[0]));

        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup sendWithInlineKeyboardLine(List<String> buttonsNames) {


        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        for (String callbackData : buttonsNames) {
            InlineKeyboardButton button = new InlineKeyboardButton(callbackData).callbackData(callbackData);
            inlineKeyboardMarkup.addRow(button);
        }

        return inlineKeyboardMarkup;
    }
    public ReplyKeyboardMarkup sendWithKeyboard(List<String> buttonsNames) {
        KeyboardButton[] buttons = new KeyboardButton[buttonsNames.size()];
        for (int i = 0; i < buttonsNames.size(); i++) {
            buttons[i] = new KeyboardButton(buttonsNames.get(i));
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons);
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.selective(true);

        return replyKeyboardMarkup;
    }
}