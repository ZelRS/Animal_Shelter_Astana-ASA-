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

    @Column(name = "тип")
    private String type;

    @Column(name = "адрес")
    private String address;

    @OneToOne
    @JoinColumn(name = "название_приюта", referencedColumnName = "название")
    private Shelter shelter;
}
