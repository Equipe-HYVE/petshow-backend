package hyve.petshow.controller.representation;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NegociacaoRepresentation {
	private Long id;
	@NotNull(message = "O preço de oferta é obrigatório.")
	@DecimalMin(value = "0.0", message = "O preço informado é inferior ao mínimo de R$0,0")
	@DecimalMax(value = "99999.99", message = "O preço informado ultrapassa o limite de R$99999,99")
	private BigDecimal precoOferta;
	private BigDecimal precoInicial;
	private Boolean respostaOferta;
	private Long agendamentoId;
}
