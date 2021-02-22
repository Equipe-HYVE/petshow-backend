package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.AvaliacaoRepresentation;
import hyve.petshow.domain.Avaliacao;
import hyve.petshow.domain.embeddables.CriteriosAvaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static hyve.petshow.util.NullUtils.isNotNull;

@Component
public class AvaliacaoConverter implements Converter<Avaliacao, AvaliacaoRepresentation> {
	@Autowired
	private ClienteConverter clienteConverter;

	@Override
	public AvaliacaoRepresentation toRepresentation(Avaliacao domain) {
		var representation = new AvaliacaoRepresentation();

		representation.setId(domain.getId());
		if (isNotNull(domain.getCriteriosAvaliacao())) {
			var info = domain.getCriteriosAvaliacao();
			representation.setAtencao(info.getAtencao());
			representation.setQualidadeProdutos(info.getQualidadeProdutos());
			representation.setCustoBeneficio(info.getCustoBeneficio());
			representation.setInfraestrutura(info.getInfraestrutura());
			representation.setQualidadeServico(info.getQualidadeServico());
			representation.setComentario(info.getComentario());
		}
		representation.setCliente(clienteConverter.toRepresentation(domain.getCliente()));
		representation.setServicoAvaliadoId(domain.getServicoAvaliadoId());
		representation.setMedia(domain.getMediaAvaliacao());

		return representation;
	}

	@Override
	public Avaliacao toDomain(AvaliacaoRepresentation representation) {
		var domain = new Avaliacao();
		//var clienteConverter = new ClienteConverter();

		domain.setId(representation.getId());
		domain.setCliente(clienteConverter.toDomain(representation.getCliente()));
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
}
