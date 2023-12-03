package pro.sky.telegramBot.model.Adoption;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
}
