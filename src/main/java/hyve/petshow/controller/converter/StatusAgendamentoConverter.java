package hyve.petshow.controller.converter;

import hyve.petshow.controller.representation.StatusAgendamentoRepresentation;
import hyve.petshow.domain.StatusAgendamento;
import org.springframework.stereotype.Component;

@Component
public class StatusAgendamentoConverter implements Converter<StatusAgendamento, StatusAgendamentoRepresentation> {
    @Override
    public StatusAgendamentoRepresentation toRepresentation(StatusAgendamento domain) {
        var representation = new StatusAgendamentoRepresentation();

        representation.setId(domain.getId());
        representation.setNome(domain.getNome());

        return representation;
    }

    @Override
    public StatusAgendamento toDomain(StatusAgendamentoRepresentation representation) {
        var domain = new StatusAgendamento();

        domain.setId(representation.getId());
        domain.setNome(representation.getNome());

        return domain;
    }
}
