package hyve.petshow.controller.representation;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ServicoDetalhadoRepresentation {
	private Long Id;
	@NotNull(message = "O tipo de serviço é obrigatório.")
	private ServicoRepresentation tipo;
	private List<AvaliacaoRepresentation> avaliacoes = new ArrayList<>();
	private Float mediaAvaliacao;
	private Long prestadorId;
	private PrestadorRepresentation prestador;
	@NotEmpty(message = "Os preços por tipo são obrigatórios")
	private List<PrecoPorTipoRepresentation> precoPorTipo = new ArrayList<>();
	private List<AdicionalRepresentation> adicionais = new ArrayList<>();
	private Boolean ativo;

	public Float getMedia() {
		return Optional.ofNullable(getMediaAvaliacao())
				.orElseGet(() ->(float) avaliacoes.stream().mapToDouble(el -> el.getMedia()).average().orElse(0));
	}
}
