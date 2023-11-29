package pro.sky.telegramBot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * сущность, содержащая в себе параметры, необходимые для формирования медиа-контента в сообщении
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaMessageParams {
    /**
     * id чата для определения, куда отправлять сообщение
     */
    private long chatId;
    /**
     * путь к файлу
     */
    private String filePath;
    /**
     * имя файла
     */
    private String fileName;
    /**
     * текст под фотографией
     */
    private String caption;
}
