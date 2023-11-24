package pro.sky.telegramBot.model.shelter;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "информация_о_приюте")
@RequiredArgsConstructor
@Getter
@Setter
public class ShelterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "адрес")
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShelterInfo)) return false;
        ShelterInfo that = (ShelterInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ShelterInfo{" +
               "id=" + id +
               ", address='" + address + '\'' +
               '}';
    }
}
