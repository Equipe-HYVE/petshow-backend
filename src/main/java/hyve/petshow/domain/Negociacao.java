package hyve.petshow.domain;
import hyve.petshow.domain.embeddables.Auditoria;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class Negociacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal precoInicial;
	private BigDecimal precoOfertado;
	private String respostaOferta;
	@Column(name = "fk_agendamento")
	private Long idAgendamento;
	@Embedded
	private Auditoria auditoria;
}
