package pro.sky.telegramBot.controller.constants;

import org.springframework.mock.web.MockMultipartFile;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.shelter.Shelter;

public class ShelterControllerTestConstants {
    public static final Shelter SHELTER = new Shelter();

    public static final Long ID = 1L;
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";
    public static final String PREVIEW = "Preview";
    public static final String SCHEDULE = "Schedule";
    public static final String ADDRESS = "Address";
    public static final String SECURITY_PHONE = "Phone";
    public static final String SAFETY_RULES = "Rules";
    public static final PetType TYPE = PetType.DOG;

    public static final byte[] BYTES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static final MockMultipartFile PHOTO =
            new MockMultipartFile("Фото приюта",
                    "1.jpg",
                    "image/jpeg",
                    BYTES);

    public static final MockMultipartFile SCHEMA =
            new MockMultipartFile("Графический файл со схемой проезда",
                    "1.jpg",
                    "image/jpeg",
                    BYTES);

}
