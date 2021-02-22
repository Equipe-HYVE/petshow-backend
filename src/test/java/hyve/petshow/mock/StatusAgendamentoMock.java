package hyve.petshow.mock;

import hyve.petshow.controller.representation.StatusAgendamentoRepresentation;
import hyve.petshow.domain.StatusAgendamento;

public class StatusAgendamentoMock {
    public static StatusAgendamento criaStatusAgendamento(){
        var statusAgendamento = new StatusAgendamento();

        statusAgendamento.setId(1);
        statusAgendamento.setNome("teste");

        return statusAgendamento;
    }

    public static StatusAgendamentoRepresentation criaStatusAgendamentoRepresentation(){
        var statusAgendamentoRepresentation = new StatusAgendamentoRepresentation();

        statusAgendamentoRepresentation.setId(1);
        statusAgendamentoRepresentation.setNome("teste");

        return statusAgendamentoRepresentation;
    }
}
