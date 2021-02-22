package hyve.petshow.service.port;

import hyve.petshow.domain.StatusAgendamento;
import hyve.petshow.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatusAgendamentoService {
    List<StatusAgendamento> buscarStatusAgendamento() throws NotFoundException;

    StatusAgendamento buscarStatusAgendamento(Integer statusId) throws NotFoundException;
}
