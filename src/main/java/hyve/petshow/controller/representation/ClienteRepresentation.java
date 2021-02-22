package hyve.petshow.controller.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteRepresentation extends ContaRepresentation {
	private List<AnimalEstimacaoRepresentation> animaisEstimacao = new ArrayList<AnimalEstimacaoRepresentation>();

	public ClienteRepresentation() {}
	public ClienteRepresentation(ContaRepresentation conta) {
		super(conta.getId(), conta.getNome(), conta.getNomeSocial(), conta.getCpf(), conta.getTelefone(), conta.getTipo(), conta.getFoto(), conta.getMediaAvaliacao(), conta.getEndereco(), conta.getLogin(), conta.getGeolocalizacao(), conta.getIsAtivo(), conta.getMensagem());
	}
}
