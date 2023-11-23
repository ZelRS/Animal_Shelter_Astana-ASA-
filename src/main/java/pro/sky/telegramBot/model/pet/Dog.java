package pro.sky.telegramBot.model.pet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.model.shelter.Shelter;

import javax.persistence.*;

@Entity(name = "Собака")
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Dog extends Pet {
    @ManyToOne
    @JoinColumn(name = "название_приюта", referencedColumnName = "название")
    private Shelter shelter;
}
