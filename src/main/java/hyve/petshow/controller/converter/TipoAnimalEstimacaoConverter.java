package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.TipoAnimalEstimacaoRepresentation;
import hyve.petshow.domain.TipoAnimalEstimacao;
import org.springframework.stereotype.Component;

@Component
public class TipoAnimalEstimacaoConverter implements Converter<TipoAnimalEstimacao, TipoAnimalEstimacaoRepresentation> {
    @Override
    public TipoAnimalEstimacaoRepresentation toRepresentation(TipoAnimalEstimacao domain) {
    	var representation = new TipoAnimalEstimacaoRepresentation();
		representation.setId(domain.getId());
        representation.setNome(domain.getNome());
        representation.setPelagem(domain.getPelagem());
        representation.setPorte(domain.getPorte());
		return representation;
    }

    @Override
    public TipoAnimalEstimacao toDomain(TipoAnimalEstimacaoRepresentation representation) {
    	var domain = new TipoAnimalEstimacao();

        domain.setId(representation.getId());
        domain.setNome(representation.getNome());
        domain.setPelagem(representation.getPelagem());
        domain.setPorte(representation.getPorte());
        return domain;
    }
}
