package pro.sky.telegramBot.model.pet;

import lombok.*;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.model.users.User;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
@RequiredArgsConstructor
@Getter
@Setter
@ToString
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

    @ManyToOne
    @JoinColumn(name = "приют", referencedColumnName = "id")
    private Shelter shelter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet)) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
