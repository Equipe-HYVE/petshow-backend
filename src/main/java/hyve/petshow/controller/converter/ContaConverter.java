package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.ContaRepresentation;
import hyve.petshow.domain.Conta;
import hyve.petshow.domain.embeddables.Login;
import hyve.petshow.domain.enums.TipoConta;
import org.springframework.stereotype.Component;

@Component
public class ContaConverter implements Converter<Conta, ContaRepresentation> {
	@Override
	public ContaRepresentation toRepresentation(Conta domain) {
		var representation = new ContaRepresentation();
		var login = new Login(domain.getLogin().getEmail());

		representation.setId(domain.getId());
		representation.setCpf(domain.getCpf());
		representation.setEndereco(domain.getEndereco());
		representation.setFoto(domain.getFoto());
		representation.setLogin(login);
		representation.setNome(domain.getNome());
		representation.setNomeSocial(domain.getNomeSocial());
		representation.setTelefone(domain.getTelefone());
		representation.setTipo(domain.getTipo().getTipo());
		representation.setMediaAvaliacao(domain.getMediaAvaliacao());
		representation.setGeolocalizacao(domain.getGeolocalizacao());
		representation.setIsAtivo(domain.isAtivo());

		return representation;
	}

	@Override
	public Conta toDomain(ContaRepresentation representation) {
		var domain = new Conta();

		domain.setCpf(representation.getCpf());
		domain.setEndereco(representation.getEndereco());
		domain.setFoto(representation.getFoto());
		domain.setId(representation.getId());
		domain.setLogin(representation.getLogin());
		domain.setNome(representation.getNome());
		domain.setNomeSocial(representation.getNomeSocial());
		domain.setTelefone(representation.getTelefone());
		domain.setTipo(TipoConta.getTipoByInteger(representation.getTipo()));
		domain.setMediaAvaliacao(representation.getMediaAvaliacao());
		domain.setGeolocalizacao(representation.getGeolocalizacao());

		return domain;
	}
}
