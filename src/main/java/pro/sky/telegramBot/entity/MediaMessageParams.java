package pro.sky.telegramBot.entity;

import lombok.Data;

@Data
public class MediaMessageParams {

    private long chatId;
    private String filePath;
    private String fileName;
    private String caption;

    public MediaMessageParams() {
    }
}
