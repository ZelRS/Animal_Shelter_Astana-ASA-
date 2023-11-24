package pro.sky.telegramBot.model.pet;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Entity;
import java.util.Objects;

@Entity(name = "Собака")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Dog extends Pet {


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Dog dog = (Dog) o;
        return getId() != null && Objects.equals(getId(), dog.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
