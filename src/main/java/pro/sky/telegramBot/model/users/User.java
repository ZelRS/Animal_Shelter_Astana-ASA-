package pro.sky.telegramBot.model.users;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.model.pet.Pet;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "Пользователь")
@RequiredArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_name")
    private String userName;

    @OneToOne
    @JoinColumn(name = "информация_о_пользователе_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user")
    private Collection<Pet> pets;
}
