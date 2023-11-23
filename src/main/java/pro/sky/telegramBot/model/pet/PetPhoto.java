package pro.sky.telegramBot.model.pet;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity(name = "Фото_животного")
@RequiredArgsConstructor
@Data
public class PetPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filePath;

    private Long fileSize;

    private String mediaType;

    @Lob
    private byte[] data;
}
