package hyve.petshow.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
public class AdicionalAgendamento implements Serializable {
    private static final long serialVersionUID = 368702712017722297L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_agendamento", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Agendamento agendamento;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_adicional", referencedColumnName = "id", nullable = false)
    private Adicional adicional;
    
    public AdicionalAgendamento(Agendamento agendamento, Adicional adicional){
        this.agendamento = agendamento;
        this.adicional = adicional;
    }
}
