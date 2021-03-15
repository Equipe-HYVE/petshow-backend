package hyve.petshow.controller.representation;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AvaliacaoRepresentation {
	private Long id;
	@Min(value = 0, message = "O valor mínimo para atenção não pode ser inferior a 5.")
	@Max(value = 5, message = "O valor máximo para atenção não pode ser superior a 5.")
	@NotNull(message = "O atributo atenção da avaliação é obrigatório.")
	private Integer atencao;
	@Min(value = 0, message = "O valor mínimo para qualidade de produtos não pode ser inferior a 5.")
	@Max(value = 5, message = "O valor máximo para qualidade de produtos não pode ser superior a 5.")
	@NotNull(message = "O atributo qualidade de produtos da avaliação é obrigatório.")
	private Integer qualidadeProdutos;
	@Min(value = 0, message = "O valor mínimo para custo benefício não pode ser inferior a 5.")
	@Max(value = 5, message = "O valor máximo para custo benefício não pode ser superior a 5.")
	@NotNull(message = "O atributo custo benefício da avaliação é obrigatório.")
	private Integer custoBeneficio;
	@Min(value = 0, message = "O valor mínimo para infraestrutura não pode ser inferior a 5.")
	@Max(value = 5, message = "O valor máximo para infraestrutura não pode ser superior a 5.")
	@NotNull(message = "O atributo infraestrutura da avaliação é obrigatório.")
	private Integer infraestrutura;
	@Min(value = 0, message = "O valor mínimo para qualidade do serviço não pode ser inferior a 5.")
	@Max(value = 5, message = "O valor máximo para qualidade do serviço não pode ser superior a 5.")
	@NotNull(message = "O atributo qualidade do serviço da avaliação é obrigatório.")
	private Integer qualidadeServico;
	@Size(max = 280, message = "O comentário informado ultrapassa o limite de 280 caracteres.")
	private String comentario;
	private Double media;
	@NotNull(message = "O cliente da avaliação é obrigatório")
	private ClienteRepresentation cliente;
	@NotNull(message = "O id do serviço avaliado é obrigatório")
	private Long servicoAvaliadoId;
}
