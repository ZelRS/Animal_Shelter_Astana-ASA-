package pro.sky.telegramBot.utils;

import org.springframework.stereotype.Component;

import java.util.List;

// класс, выполняющий логику создания списка, содержащего строки в определенном формате
@Component
public class StringListCreator {

    public String createStringList(List<String> names) {

        StringBuilder formattedNames = new StringBuilder();
        int count = 1;

        for (String name : names) {
            formattedNames.append("/").append(count).append("- ").append(name).append("\n");
            count++;
        }

        return formattedNames.toString();
    }

}
