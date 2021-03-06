package hyve.petshow.controller.representation;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NegociacaoRepresentation {
	private Long id;
	private BigDecimal precoFinal;
	private BigDecimal precoInicial;
	private String respostaOferta;
	private AgendamentoRepresentation agendamento;
}
