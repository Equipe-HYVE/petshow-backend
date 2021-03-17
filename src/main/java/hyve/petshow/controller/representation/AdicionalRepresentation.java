package hyve.petshow.controller.representation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionalRepresentation {
	private Long id;
	@Size(max = 80, message = "O nome informado ultrapassa o limite de 80 caracteres.")
	@NotNull(message = "O nome do adicional é obrigatório.")
	private String nome;
	@Size(max = 280, message = "A descrição informada ultrapassa o limite de 280 caracteres.")
	private String descricao;
	@NotNull(message = "O preço do adicional é obrigatório.")
	@DecimalMin(value = "0.0", message = "O preço informado é inferior ao mínimo de R$0,0")
	@DecimalMax(value = "99999.99", message = "O preço informado ultrapassa o limite de R$99999,99")
	private BigDecimal preco;
	private Long servicoDetalhadoId;
	private Boolean ativo;
}
