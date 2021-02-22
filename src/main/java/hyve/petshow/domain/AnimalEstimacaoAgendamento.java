package hyve.petshow.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "animal_estimacao_agendamento")
@AllArgsConstructor
@NoArgsConstructor
public class AnimalEstimacaoAgendamento implements Serializable {
    private static final long serialVersionUID = 6999732330321679979L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_agendamento", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Agendamento agendamento;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_animal_estimacao", referencedColumnName = "id", nullable = false)
    private AnimalEstimacao animalEstimacao;
}
