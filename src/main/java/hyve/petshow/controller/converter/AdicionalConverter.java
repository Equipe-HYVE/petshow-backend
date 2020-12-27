package hyve.petshow.controller.converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import hyve.petshow.controller.representation.AdicionalRepresentation;
import hyve.petshow.domain.Adicional;

@Component
public class AdicionalConverter implements Converter<Adicional, AdicionalRepresentation> {
	@Override
	public AdicionalRepresentation toRepresentation(Adicional domain) {
		return Optional.ofNullable(domain).map(adicional -> {
			var representation = new AdicionalRepresentation();
			representation.setId(adicional.getId());
			representation.setDescricao(adicional.getDescricao());
			representation.setNome(adicional.getNome());
			representation.setPreco(adicional.getPreco());
			representation.setIdServicoDetalhado(adicional.getIdServicoDetalhado());
			return representation;
		}).orElse(new AdicionalRepresentation());
	}

	@Override
	public Adicional toDomain(AdicionalRepresentation representation) {
		return Optional.ofNullable(representation).map(adicional -> {
			var domain = new Adicional();
			domain.setId(adicional.getId());
			domain.setNome(adicional.getNome());
			domain.setDescricao(adicional.getDescricao());
			domain.setPreco(adicional.getPreco());
			domain.setIdServicoDetalhado(adicional.getIdServicoDetalhado());
			return domain;
		}).orElse(new Adicional());
	}

}
