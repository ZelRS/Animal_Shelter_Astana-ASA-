package pro.sky.telegramBot.model.adoption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Entity(name = "report")
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private LocalDate reportDateTime;

    @Min(0)
    @Max(10)
    private int dietAppetite;

    @Min(0)
    @Max(10)
    private int dietPreferences;

    @Min(0)
    @Max(10)
    private int dietAllergies;

    @Min(0)
    @Max(10)
    private int healthStatus;

    @Min(0)
    @Max(10)
    private int behaviorChange;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "adoptionRecord_id")
    private AdoptionRecord adoptionRecord;

    @Lob
    @Column(name = "photo")
    @JsonIgnore
    private byte[] data;

    @JoinColumn(name = "rating_total")
    private int ratingTotal;

    /**
     * enum для вопросов отчета
     */
    @Getter
    public enum QuestionsForReport {
        DIETAPPETITE("Заметили ли Вы изменения аппетита: от 0 = значительное снижение до 10 = хороший аппетит"),
        DIETPREFERENCES("Есть ли изменения в предпочитаемом рационе: от 0 = значительные до 10 = отсутствуют"),
        DIETALLERGIES("Заметили ли Вы аллергические реакции: от 0 = значительные до 10 = никаких"),
        HEALTHSTATUS("Общее состояние здоровья: от 0 = плохое до 10 = отличное"),
        BEHAVIORCHANGE("Заметили ли Вы изменения в поведении: от 0 = значительные изменения до 10 = без изменений");

        private final String question;

        QuestionsForReport(String question) {
            this.question = question;
        }
    }
}
