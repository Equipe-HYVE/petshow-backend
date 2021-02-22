package hyve.petshow.controller.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionalRepresentation {
	private Long id;
	private String nome;
	private String descricao;
	private BigDecimal preco;
	private Long servicoDetalhadoId;
}
