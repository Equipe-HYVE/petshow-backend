package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.ServicoRepresentation;
import hyve.petshow.domain.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoConverter implements Converter<Servico, ServicoRepresentation>{
	@Override
    public ServicoRepresentation toRepresentation(Servico domain) {
    	var representation = new ServicoRepresentation();

    	representation.setId(domain.getId());
        representation.setNome(domain.getNome());

        return representation;
    }

    @Override
    public Servico toDomain(ServicoRepresentation representation) {
    	var domain = new Servico();

    	domain.setId(representation.getId());
        domain.setNome(representation.getNome());

        return domain;
    }
}
