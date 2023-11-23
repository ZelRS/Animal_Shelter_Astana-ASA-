package pro.sky.telegramBot.model.shelter;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity(name = "информаиця_о_приюте")
@RequiredArgsConstructor
@Data
public class ShelterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "название")
    private String name;

    @Column(name = "адрес")
    private String address;

    @OneToOne
    @JoinColumn(name = "тип_приюта", referencedColumnName = "тип")
    private Shelter shelter;
}
