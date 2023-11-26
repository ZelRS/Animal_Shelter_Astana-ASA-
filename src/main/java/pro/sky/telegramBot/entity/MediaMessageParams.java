package pro.sky.telegramBot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// сущность, содержащая в себе параметры, необходимые для формирования медиа-контента в сообщении
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaMessageParams {

    private long chatId;
    private String filePath;
    private String fileName;
    private String caption;
}
