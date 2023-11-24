package pro.sky.telegramBot.utils;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ListCreator {

    public String createList(List<String> names) {

        StringBuilder formattedNames = new StringBuilder();

        for (String name : names) {
            formattedNames.append("- ").append(name).append("\n");
        }

        return formattedNames.toString();
    }
}
