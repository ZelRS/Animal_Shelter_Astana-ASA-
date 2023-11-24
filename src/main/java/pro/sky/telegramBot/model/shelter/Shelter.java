package pro.sky.telegramBot.model.shelter;

import lombok.*;
import pro.sky.telegramBot.enums.ShelterTypes;
import pro.sky.telegramBot.model.pet.Pet;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity(name = "приют")
@RequiredArgsConstructor
@Getter
@Setter
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "название")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "тип_приюта")
    private ShelterTypes type;

    @OneToOne
    @JoinColumn(name = "информация_о_приюте_id")
    private ShelterInfo shelterInfo;

    @OneToMany(mappedBy = "shelter")
    @ToString.Exclude
    private Collection<Pet> pets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shelter)) return false;
        Shelter shelter = (Shelter) o;
        return Objects.equals(id, shelter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Shelter{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", type=" + type +
               ", shelterInfo=" + shelterInfo +
               '}';
    }
}
