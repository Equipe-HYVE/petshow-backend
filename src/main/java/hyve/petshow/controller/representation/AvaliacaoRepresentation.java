package hyve.petshow.controller.representation;

import lombok.Data;

@Data
public class AvaliacaoRepresentation {
	private Long id;
	private Integer atencao;
	private Integer qualidadeProdutos;
	private Integer custoBeneficio;
	private Integer infraestrutura;
	private Integer qualidadeServico;
	private String comentario;
	private Double media;
	private ClienteRepresentation cliente;
	private Long servicoAvaliadoId;
}
