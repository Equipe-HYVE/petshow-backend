package hyve.petshow.controller.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PrestadorRepresentation extends ContaRepresentation {
    private List<ServicoDetalhadoRepresentation> servicos;
    private String descricao;
    
    public PrestadorRepresentation() {}
    
    public PrestadorRepresentation(ContaRepresentation conta) {
		super(conta.getId(), conta.getNome(), conta.getNomeSocial(), conta.getCpf(), conta.getTelefone(), conta.getTipo(), conta.getFoto(), conta.getMediaAvaliacao(), conta.getEndereco(), conta.getLogin(), conta.getGeolocalizacao(), conta.getIsAtivo(), conta.getMensagem());
	}
}