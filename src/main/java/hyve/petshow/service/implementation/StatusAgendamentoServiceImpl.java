package hyve.petshow.service.implementation;

import hyve.petshow.domain.StatusAgendamento;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.StatusAgendamentoRepository;
import hyve.petshow.service.port.StatusAgendamentoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static hyve.petshow.util.LogUtils.INFO_REQUEST_SERVICE;
import static hyve.petshow.util.LogUtils.INFO_REQUEST_SERVICE_EMPTY;

@Slf4j
@Service
public class StatusAgendamentoServiceImpl extends TipoService<StatusAgendamento> implements StatusAgendamentoService {
    private static final String NENHUM_STATUS_AGENDAMENTO_ENCONTRADO = "NENHUM_STATUS_AGENDAMENTO_ENCONTRADO";

    @Autowired
    private StatusAgendamentoRepository repository;

    public StatusAgendamentoServiceImpl(){super(NENHUM_STATUS_AGENDAMENTO_ENCONTRADO);}

    @Override
    public List<StatusAgendamento> buscarStatusAgendamento() throws NotFoundException {
        log.info(INFO_REQUEST_SERVICE_EMPTY, "buscarStatusAgendamento");
        return buscarTodos();
    }

    @Override
    public List<StatusAgendamento> buscarLista() {
        log.info(INFO_REQUEST_SERVICE_EMPTY, "buscarLista");
        return repository.findAll();
    }

    @Override
    public StatusAgendamento buscarStatusAgendamento(Integer statusId) throws NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarStatusAgendamento", statusId);
        return repository.findById(statusId)
                .orElseThrow(() -> new NotFoundException(NENHUM_STATUS_AGENDAMENTO_ENCONTRADO));
    }

	@Override
	public StatusAgendamento buscarStatusPorNome(String nome) throws NotFoundException {
		return repository.findByNome(nome).orElseThrow(() -> new NotFoundException(NENHUM_STATUS_AGENDAMENTO_ENCONTRADO));
	}
    
    
}
