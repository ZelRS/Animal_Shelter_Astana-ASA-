package pro.sky.telegramBot.model.users;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity(name = "Информация_о_пользователе")
@RequiredArgsConstructor
@Data
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "chatId_пользователя", referencedColumnName = "chat_id")
    private User user;

    @Column(name = "паспорт")
    private String passport;

    @Column(name = "адрес")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "номер_телефона")
    private String phone;
}
