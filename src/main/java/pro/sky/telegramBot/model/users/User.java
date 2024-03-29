package pro.sky.telegramBot.model.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.enums.UserState;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.model.shelter.Shelter;

import javax.persistence.*;

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
    @JsonIgnore
    private UserInfo userInfo;

    @OneToOne
    @JoinColumn(name = "pet_id")
    @JsonIgnore
    private Pet pet;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "shelter_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private Shelter shelter;

    @OneToOne
    @JsonIgnore
    private AdoptionRecord adoptionRecord;
}
