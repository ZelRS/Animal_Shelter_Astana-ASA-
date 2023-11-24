package pro.sky.telegramBot.model.pet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import pro.sky.telegramBot.model.shelter.Shelter;

import javax.persistence.*;

@Entity(name = "Кошка")
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Cat extends Pet {
    @ManyToOne
    @JoinColumn(name = "тип_приюта", referencedColumnName = "тип_приюта")
    private Shelter shelter;
}
