package pro.sky.telegramBot.utils;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegramBot.entity.Buttons;


import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j  // SLF4J logging
public class ButtonsCreator {

    public InlineKeyboardMarkup sendWithInlineKeyboard(List<Buttons> buttonsNames) {

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Buttons buttonsName : buttonsNames) {
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