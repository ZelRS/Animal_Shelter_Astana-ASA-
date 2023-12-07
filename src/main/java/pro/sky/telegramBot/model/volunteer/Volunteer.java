package pro.sky.telegramBot.model.volunteer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.enums.VolunteerState;

import javax.persistence.*;

@Entity(name = "volunteer")
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "volunteer_name")
    private String name;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private VolunteerState state;
}