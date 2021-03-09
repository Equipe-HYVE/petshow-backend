package hyve.petshow.service.port;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.StatusAgendamento;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;

@Service
public interface AgendamentoService {
    Agendamento adicionarAgendamento(Agendamento agendamento);

    Page<Agendamento> buscarAgendamentosPorCliente(Long id, Pageable pageable) throws NotFoundException, BusinessException;

    Page<Agendamento> buscarAgendamentosPorPrestador(Long id, Pageable pageable) throws NotFoundException;

    Agendamento buscarPorIdAtivo(Long id, Long usuarioId) throws NotFoundException, BusinessException;

    Agendamento atualizarAgendamento(Long id, Long prestadorId, Agendamento request) throws BusinessException, NotFoundException;

    Agendamento ativarAgendamento(Long id, Long clienteId) throws NotFoundException, BusinessException;

    Agendamento atualizarStatusAgendamento(Long id, Long prestadorId, StatusAgendamento statusAgendamento) throws BusinessException, NotFoundException;
    
    List<String> buscarHorariosAgendamento(Long prestadorId, LocalDate dataAgendamento);

    Agendamento buscarPorId(Long id, Long usuarioId) throws BusinessException, NotFoundException;

    void deletarAgendamento(Long agendamentoId, Long clienteId) throws NotFoundException, BusinessException;
    
    Agendamento atualizarAgendamento(Long agendamentoId, Agendamento agendamento) throws NotFoundException, BusinessException;

}
