package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.AvaliacaoRepresentation;
import hyve.petshow.domain.Avaliacao;
import hyve.petshow.domain.CriteriosAvaliacao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvaliacaoConverter implements Converter<Avaliacao, AvaliacaoRepresentation> {
	@Override
	public AvaliacaoRepresentation toRepresentation(Avaliacao domain) {
		if(domain == null) return new AvaliacaoRepresentation();
		
		var representation = new AvaliacaoRepresentation();
		representation.setId(domain.getId());
		if (domain.getCriteriosAvaliacao() != null) {
			var info = domain.getCriteriosAvaliacao();
			representation.setAtencao(info.getAtencao());
			representation.setQualidadeProdutos(info.getQualidadeProdutos());
			representation.setCustoBeneficio(info.getCustoBeneficio());
			representation.setInfraestrutura(info.getInfraestrutura());
			representation.setQualidadeServico(info.getQualidadeServico());
			representation.setComentario(info.getComentario());
		}
		representation.setClienteId(domain.getClienteId());
		representation.setServicoAvaliadoId(domain.getServicoAvaliadoId());
		representation.setMedia(domain.getMediaAvaliacao());
		return representation;
	}

	@Override
	public Avaliacao toDomain(AvaliacaoRepresentation representation) {
		if(representation == null) return new Avaliacao();
		var domain = new Avaliacao();
		domain.setId(representation.getId());
		domain.setClienteId(representation.getClienteId());
		domain.setServicoAvaliadoId(representation.getServicoAvaliadoId());
		
		var info = new CriteriosAvaliacao();
		info.setAtencao(representation.getAtencao());
		info.setQualidadeProdutos(representation.getQualidadeProdutos());
		info.setCustoBeneficio(representation.getCustoBeneficio());
		info.setInfraestrutura(representation.getInfraestrutura());
		info.setQualidadeServico(representation.getQualidadeServico());
		info.setComentario(representation.getComentario());
		
		domain.setCriteriosAvaliacao(info);
		return domain;
	}
	
	public List<AvaliacaoRepresentation> toRepresentationList(List<Avaliacao> domainList) {
		if(domainList == null) return new ArrayList<AvaliacaoRepresentation>();
		return domainList.stream()
				.map(el -> toRepresentation(el))
				.collect(Collectors.toList());
	}
	
	public List<Avaliacao> toDomainList(List<AvaliacaoRepresentation> representationList) {
		if(representationList == null) return new ArrayList<Avaliacao>();
		return representationList.stream()
				.map(el -> toDomain(el))
				.collect(Collectors.toList());
	}
}
