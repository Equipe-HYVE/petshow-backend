package hyve.petshow.controller.representation;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ServicoDetalhadoRepresentation {
	private Long Id;
	private ServicoRepresentation tipo;
	private List<AvaliacaoRepresentation> avaliacoes = new ArrayList<>();
	private Float mediaAvaliacao;
	private Long prestadorId;
	private PrestadorRepresentation prestador;
	private List<PrecoPorTipoRepresentation> precoPorTipo = new ArrayList<>();
	private List<AdicionalRepresentation> adicionais = new ArrayList<>();

	public Float getMedia() {
		return Optional.ofNullable(getMediaAvaliacao())
				.orElseGet(() ->(float) avaliacoes.stream().mapToDouble(el -> el.getMedia()).average().orElse(0));
	}
}
