package pro.sky.telegramBot.model.adoption;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.model.pet.Pet;
import pro.sky.telegramBot.model.users.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "adoption_record")
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdoptionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @OneToOne
    private Pet pet;
    @OneToOne
    private User user;
    private LocalDate adoptionDate;
    private LocalDate trialPeriodEnd;
    private Integer trialPeriodDays;
    @Enumerated(EnumType.STRING)
    private TrialPeriodState state;
    @OneToMany(mappedBy = "adoptionRecord", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Report> reports = new ArrayList<>();
    private Integer ratingTotal;

    @Getter
    public enum TrialPeriodState {

        PROBATION("испытательный срок"),
        PROBATION_EXTEND("продление испытательного срока"),
        SUCCESSFUL("успешно завершен"),
        UNSUCCESSFUL("неуспешно завершен"),
        CLOSED("закрыто");

        private final String state;

        TrialPeriodState(String state) {
            this.state = state;
        }
    }
}