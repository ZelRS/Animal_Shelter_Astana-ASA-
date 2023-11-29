package pro.sky.telegramBot.model.shelter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.enums.PetType;
import pro.sky.telegramBot.model.pet.Pet;

import javax.persistence.*;
import java.util.Collection;

/**
 * модель приюта в базе данных
 */
@Entity(name = "shelter")
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="preview")
    private String preview;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PetType type;

    // фото приюта
    @Lob
    @Column(name = "photo")
    @JsonIgnore
    private byte[] data;

    @OneToMany(mappedBy = "shelter", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<Pet> pets;
}
