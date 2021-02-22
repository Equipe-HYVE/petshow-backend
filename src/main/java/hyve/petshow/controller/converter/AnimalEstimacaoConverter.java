package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.AnimalEstimacaoRepresentation;
import hyve.petshow.domain.AnimalEstimacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AnimalEstimacaoConverter implements Converter<AnimalEstimacao, AnimalEstimacaoRepresentation>{
    @Autowired
    private TipoAnimalEstimacaoConverter tipoAnimalEstimacaoConverter;

    @Override
    public AnimalEstimacaoRepresentation toRepresentation(AnimalEstimacao domain) {
        var representation = new AnimalEstimacaoRepresentation();

        representation.setId(domain.getId());
        representation.setNome(domain.getNome());
        representation.setFoto(domain.getFoto());
        representation.setTipo(tipoAnimalEstimacaoConverter.toRepresentation(domain.getTipo()));
        representation.setDonoId(domain.getDonoId());

        return representation;
    }

    @Override
    public AnimalEstimacao toDomain(AnimalEstimacaoRepresentation representation) {
        var domain = new AnimalEstimacao();

        domain.setId(representation.getId());
        domain.setNome(representation.getNome());
        domain.setFoto(representation.getFoto());
        domain.setTipo(tipoAnimalEstimacaoConverter.toDomain(representation.getTipo()));
        domain.setDonoId(representation.getDonoId());

        return domain;
    }
}
