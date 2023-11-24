package pro.sky.telegramBot.utils;

import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ListCreator {

    public String createList(List<String> names) {

        StringBuilder formattedNames = new StringBuilder();
        int count = 1;

        for (String name : names) {
            formattedNames.append(count).append("- ").append(name).append("\n");
            count++;
        }

        return formattedNames.toString();
    }

}
