package hyve.petshow.service.implementation;

import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.StatusAgendamento;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AgendamentoRepository;
import hyve.petshow.service.port.AgendamentoService;
import hyve.petshow.service.port.StatusAgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.*;
import static hyve.petshow.util.ProxyUtils.verificarIdentidade;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {
    private static final String NENHUM_AGENDAMENTO_ENCONTRADO = "NENHUM_AGENDAMENTO_ENCONTRADO";
    private static final String AGENDAMENTO_NAO_ENCONTRADO = "AGENDAMENTO_NAO_ENCONTRADO";
    private static final String USUARIO_NAO_PROPRIETARIO_AGENDAMENTO = "USUARIO_NAO_PROPRIETARIO_AGENDAMENTO";

    @Autowired
    public AgendamentoRepository repository;

    @Override
    public Agendamento adicionarAgendamento(Agendamento agendamento) {
        agendamento.setAuditoria(geraAuditoriaInsercao(Optional.of(agendamento.getCliente().getId())));

        return repository.save(agendamento);
    }

    @Override
    public Page<Agendamento> buscarAgendamentosPorCliente(Long id, Pageable pageable) throws NotFoundException, BusinessException {
        var agendamentos = repository.findByClienteId(id, pageable);

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
        var agendamentos = repository.findByPrestadorId(id, pageable);

        if(agendamentos.isEmpty()){
            throw new NotFoundException(NENHUM_AGENDAMENTO_ENCONTRADO);
        }

        return agendamentos;
    }

    @Override
    public Agendamento buscarPorId(Long id, Long usuarioId) throws NotFoundException, BusinessException {
        var agendamento = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(AGENDAMENTO_NAO_ENCONTRADO));

        if(! (verificarIdentidade(agendamento.getCliente().getId(), usuarioId) ||
                verificarIdentidade(agendamento.getPrestador().getId(), usuarioId))){
            throw new BusinessException(USUARIO_NAO_PROPRIETARIO_AGENDAMENTO);
        }

        return agendamento;
    }

    @Override
    public Agendamento atualizarAgendamento(Long id, Long prestadorId, Agendamento request) throws BusinessException, NotFoundException {
        var agendamento = buscarPorId(id, prestadorId);

        agendamento.setComentario(request.getComentario());
        agendamento.setEndereco(request.getEndereco());
        agendamento.setAuditoria(atualizaAuditoria(agendamento.getAuditoria(), ATIVO));

        return repository.save(agendamento);
    }

    @Override
    public Agendamento atualizarStatusAgendamento(Long id, Long prestadorId, StatusAgendamento statusAgendamento)
            throws BusinessException, NotFoundException {
        var agendamento = buscarPorId(id, prestadorId);

        agendamento.setStatus(statusAgendamento);

        return repository.save(agendamento);
    }
}
