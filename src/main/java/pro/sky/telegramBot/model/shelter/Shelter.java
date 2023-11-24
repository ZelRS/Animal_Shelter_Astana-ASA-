package pro.sky.telegramBot.model.shelter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.model.pet.Pet;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "shelter")
@RequiredArgsConstructor
@Data
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "photo")
    private byte[] data;

    @OneToMany(mappedBy = "shelter")
    private Collection<Pet> pets;
}