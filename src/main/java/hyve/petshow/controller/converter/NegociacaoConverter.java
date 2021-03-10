package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.NegociacaoRepresentation;
import hyve.petshow.domain.Negociacao;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.ATIVO;
import static hyve.petshow.util.AuditoriaUtils.INATIVO;
import static hyve.petshow.util.NullUtils.isNotNull;

@Component
public class NegociacaoConverter implements Converter<Negociacao, NegociacaoRepresentation> {

	@Override
	public NegociacaoRepresentation toRepresentation(Negociacao domain) {
		return Optional.ofNullable(domain).map(negociacao -> {
			var representation = new NegociacaoRepresentation();
			representation.setId(negociacao.getId());
			representation.setAgendamentoId(domain.getIdAgendamento());
			representation.setPrecoInicial(domain.getPrecoInicial());
			representation.setPrecoOferta(domain.getPrecoOfertado());
			if (isNotNull(domain.getRespostaOferta())) {
				representation.setRespostaOferta(ATIVO.equals(domain.getRespostaOferta()));
			}
			return representation;
		}).orElse(null);
	}

	@Override
	public Negociacao toDomain(NegociacaoRepresentation representation) {

		return Optional.ofNullable(representation).map(negociacao -> {
			var domain = new Negociacao();
			domain.setId(negociacao.getId());
			domain.setPrecoInicial(negociacao.getPrecoInicial());
			domain.setPrecoOfertado(negociacao.getPrecoOferta());
			if(isNotNull(negociacao.getRespostaOferta())) {
				domain.setRespostaOferta(negociacao.getRespostaOferta() ? ATIVO : INATIVO);
			}
			return domain;
		}).orElse(null);
	}

}
