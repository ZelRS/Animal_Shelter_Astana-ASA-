package pro.sky.telegramBot.model.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.model.shelter.Shelter;

import javax.persistence.*;
import java.util.Collection;

/**
 * модель пользователя в базе данных
 */
@Entity(name = "person")
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private UserState state;

    @Column(name = "user_name")
    private String userName;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "info_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user")
    private Collection<Pet> pets;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
}
