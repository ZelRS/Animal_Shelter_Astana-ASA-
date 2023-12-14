package pro.sky.telegramBot.model.pet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.Adoption.AdoptionRecord;
import pro.sky.telegramBot.model.shelter.Shelter;
import pro.sky.telegramBot.model.users.User;

import javax.persistence.*;

/**
 * модель животного в базе данных
 */
@Entity(name = "pet")
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PetType type;

    @Column(name = "name")
    private String name;

    @Column(name = "breed")
    private String breed;

    @Column(name = "age")
    private Integer age;

    @Column(name = "description")
    private String description;

    @Column(name = "gender")
    private String gender;

    // фото животного
    @Lob
    @Column(name = "photo")
    @JsonIgnore
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "owner_id", unique = true)
    @JsonIgnore
    private User owner;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @JsonIgnore
    private Shelter shelter;

    @OneToOne
    @JsonIgnore
    private AdoptionRecord adoptionRecord;
}
