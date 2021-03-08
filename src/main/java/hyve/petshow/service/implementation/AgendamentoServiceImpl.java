package hyve.petshow.service.implementation;

import static hyve.petshow.util.AuditoriaUtils.*;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_SERVICE;
import static hyve.petshow.util.ProxyUtils.verificarIdentidade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.StatusAgendamento;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AgendamentoRepository;
import hyve.petshow.service.port.AgendamentoService;

@Slf4j
@Service
public class AgendamentoServiceImpl implements AgendamentoService {
    private static final String NENHUM_AGENDAMENTO_ENCONTRADO = "NENHUM_AGENDAMENTO_ENCONTRADO";
    private static final String AGENDAMENTO_NAO_ENCONTRADO = "AGENDAMENTO_NAO_ENCONTRADO";
    private static final String USUARIO_NAO_PROPRIETARIO_AGENDAMENTO = "USUARIO_NAO_PROPRIETARIO_AGENDAMENTO";

    @Autowired
    public AgendamentoRepository repository;

    @Override
    public Agendamento adicionarAgendamento(Agendamento agendamento) {
        log.info(INFO_REQUEST_SERVICE.concat("{}"), "adicionarAgendamento", agendamento);
        agendamento.setAuditoria(geraAuditoriaInsercao(Optional.of(agendamento.getCliente().getId())));
        //Insere inativo como pré-agendamento
        agendamento.getAuditoria().setFlagAtivo(INATIVO);

        return repository.save(agendamento);
    }

    @Override
    public Page<Agendamento> buscarAgendamentosPorCliente(Long id, Pageable pageable) throws NotFoundException, BusinessException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarAgendamentosPorCliente", id, pageable);
        var agendamentos = repository.findByClienteIdAndAuditoriaFlagAtivo(id, ATIVO, pageable);

        if(agendamentos.isEmpty()){
            throw new NotFoundException(NENHUM_AGENDAMENTO_ENCONTRADO);
        }

        if(!verificarIdentidade(agendamentos.get()
                .findFirst().get()
                .getCliente().getId(), id)) {
            throw new BusinessException(USUARIO_NAO_PROPRIETARIO_AGENDAMENTO);
        }

        return agendamentos;
    }

    @Override
    public Page<Agendamento> buscarAgendamentosPorPrestador(Long id, Pageable pageable) throws NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarAgendamentosPorPrestador", id, pageable);
        var agendamentos = repository.findByPrestadorIdAndAuditoriaFlagAtivo(id, ATIVO, pageable);

        if(agendamentos.isEmpty()){
            throw new NotFoundException(NENHUM_AGENDAMENTO_ENCONTRADO);
        }

        return agendamentos;
    }

    @Override
    public Agendamento buscarPorIdAtivo(Long id, Long usuarioId) throws NotFoundException, BusinessException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarPorIdAtivo", id, usuarioId);
        var agendamento = repository.findByIdAndAuditoriaFlagAtivo(id, ATIVO)
                .orElseThrow(() -> new NotFoundException(AGENDAMENTO_NAO_ENCONTRADO));

        if(! (verificarIdentidade(agendamento.getCliente().getId(), usuarioId) ||
                verificarIdentidade(agendamento.getPrestador().getId(), usuarioId))){
            throw new BusinessException(USUARIO_NAO_PROPRIETARIO_AGENDAMENTO);
        }

        return agendamento;
    }

    @Override
    public Agendamento atualizarAgendamento(Long id, Long prestadorId, Agendamento request) throws BusinessException, NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}, {}"), "atualizarAgendamento", id, prestadorId, request);
        var agendamento = buscarPorIdAtivo(id, prestadorId);

        agendamento.setComentario(request.getComentario());
        agendamento.setEndereco(request.getEndereco());
        agendamento.setAuditoria(atualizaAuditoria(agendamento.getAuditoria(), ATIVO));

        return repository.save(agendamento);
    }

    @Override
    public Agendamento ativarAgendamento(Long id, Long clienteId) throws NotFoundException, BusinessException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "ativarAgendamento", id, clienteId);
        var agendamento = buscarPorId(id, clienteId);

        agendamento.setAuditoria(atualizaAuditoria(agendamento.getAuditoria(), ATIVO));

        var response = repository.save(agendamento);

        return response;
    }

    @Override
    public Agendamento atualizarStatusAgendamento(Long id, Long prestadorId, StatusAgendamento statusAgendamento)
            throws BusinessException, NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}, {}"), "atualizarStatusAgendamento", id, prestadorId, statusAgendamento);
        var agendamento = buscarPorIdAtivo(id, prestadorId);

        agendamento.setStatus(statusAgendamento);

        return repository.save(agendamento);
    }

	@Override
	public List<String> buscarHorariosAgendamento(Long prestadorId, LocalDate dataAgendamento) {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarHorariosAgendamento", prestadorId, dataAgendamento);
        var agendamentos = repository.findAllByPrestadorAndDateAndAuditoriaFlagAtivo(prestadorId, dataAgendamento);
		return agendamentos.stream().map(el -> {
			var data = el.getData();
			data.toLocalDate();
			return data.format(DateTimeFormatter.ofPattern("HH:mm"));
		}).collect(Collectors.toList());
	}

    @Override
    public Agendamento buscarPorId(Long id, Long usuarioId) throws BusinessException, NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarPorId", id, usuarioId);
        var agendamento = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(AGENDAMENTO_NAO_ENCONTRADO));

        if(! (verificarIdentidade(agendamento.getCliente().getId(), usuarioId) ||
                verificarIdentidade(agendamento.getPrestador().getId(), usuarioId))){
            throw new BusinessException(USUARIO_NAO_PROPRIETARIO_AGENDAMENTO);
        }

        return agendamento;
    }

    @Override
    public void deletarAgendamento(Long agendamentoId, Long clienteId) throws NotFoundException, BusinessException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "deletarAgendamento", agendamentoId, clienteId);
        var agendamento = buscarPorId(agendamentoId, clienteId);

        repository.delete(agendamento);
    }
}
