package pro.sky.telegramBot.model.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.pet.Pet;

import javax.persistence.*;
import java.util.Collection;

// модель пользователя(БД)
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

    @OneToOne
    @JoinColumn(name = "info_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user")
    private Collection<Pet> pets;
}
