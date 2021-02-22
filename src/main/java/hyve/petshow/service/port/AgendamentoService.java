package hyve.petshow.service.port;

import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.StatusAgendamento;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AgendamentoService {
    Agendamento adicionarAgendamento(Agendamento agendamento);

    Page<Agendamento> buscarAgendamentosPorCliente(Long id, Pageable pageable) throws NotFoundException, BusinessException;

    Page<Agendamento> buscarAgendamentosPorPrestador(Long id, Pageable pageable) throws NotFoundException;

    Agendamento buscarPorId(Long id, Long usuarioId) throws NotFoundException, BusinessException;

    Agendamento atualizarAgendamento(Long id, Long prestadorId, Agendamento request) throws BusinessException, NotFoundException;

    Agendamento atualizarStatusAgendamento(Long id, Long prestadorId, StatusAgendamento statusAgendamento) throws BusinessException, NotFoundException;
}
