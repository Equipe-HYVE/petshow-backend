package hyve.petshow.domain;
import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
	private BigDecimal precoFinal;
	private BigDecimal precoInicial;
	private String respostaOferta;
	@OneToOne(targetEntity = Agendamento.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "fk_agendamento")
	private Agendamento agendamento;
	@Embedded
	private Auditoria auditoria;
}
