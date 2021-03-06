package hyve.petshow.domain;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import hyve.petshow.domain.embeddables.Auditoria;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Negociacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal precoInicial;
	private BigDecimal precoOferta;
	private String respostaOferta;
	@Column(name = "fk_agendamento")
	private Long idAgendamento;
	@Embedded
	private Auditoria auditoria;
}
