package pro.sky.telegramBot.model.pet;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.model.users.User;

import javax.persistence.*;

@MappedSuperclass
@RequiredArgsConstructor
@Data
public abstract class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "кличка")
    private String name;

    @Column(name = "порода")
    private String breed;

    @Column(name = "возраст")
    private Integer age;

    @Column(name = "описание")
    private String description;

    @Column(name = "пол")
    private String gender;

    @OneToOne
    @JoinColumn(name = "фото_животного_id")
    private PetPhoto petPhoto;

    @ManyToOne
    @JoinColumn(name = "приручивший_пользователь_id")
    private User user;
}
