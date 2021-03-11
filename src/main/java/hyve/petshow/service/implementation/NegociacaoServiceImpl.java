package hyve.petshow.service.implementation;

import static hyve.petshow.util.AuditoriaUtils.ATIVO;
import static hyve.petshow.util.AuditoriaUtils.INATIVO;
import static hyve.petshow.util.AuditoriaUtils.atualizaAuditoria;
import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyve.petshow.domain.Negociacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.NegociacaoRepository;
import hyve.petshow.service.port.AgendamentoService;
import hyve.petshow.service.port.NegociacaoService;

@Service
public class NegociacaoServiceImpl implements NegociacaoService {
	private static final String NEGOCIACAO_NAO_ENCONTRADA = "Negociação não encontrada";
	@Autowired
	private NegociacaoRepository repository;
	@Autowired
	private AgendamentoService agendamentoService;

	@Override
	public Negociacao criaNegociacao(Long idAgendamento, Long idDono, Negociacao negociacao)
			throws BusinessException, NotFoundException {
		var agendamento = agendamentoService.buscarPorId(idAgendamento, idDono);
		negociacao.setIdAgendamento(agendamento.getId());
		negociacao.setPrecoInicial(agendamento.getPrecoFinal());
		negociacao.setAuditoria(geraAuditoriaInsercao(Optional.ofNullable(idDono)));
		return repository.save(negociacao);
	}

	@Override
	public Negociacao atualizaNegociacao(Long idNegociacao, Negociacao atualizacao)
			throws BusinessException, NotFoundException {
		var negociacao = buscaPorId(idNegociacao);
		negociacao.setPrecoInicial(atualizacao.getPrecoInicial());
		negociacao.setRespostaOferta(atualizacao.getRespostaOferta());
		negociacao.setAuditoria(atualizaAuditoria(negociacao.getAuditoria(), ATIVO));
		return repository.save(negociacao);
	}

	@Override
	public Negociacao cancelaNegociacao(Long idNegociacao) throws BusinessException, NotFoundException {
		var negociacao = buscaPorId(idNegociacao);
		negociacao.setAuditoria(atualizaAuditoria(negociacao.getAuditoria(), INATIVO));
		return repository.save(negociacao);
	}

	@Override
	public Negociacao buscaPorId(Long idNegociacao) throws NotFoundException {
		return repository.findById(idNegociacao).orElseThrow(() -> new NotFoundException(NEGOCIACAO_NAO_ENCONTRADA));
	}

	@Override
	public Negociacao buscaPorAgendamentoId(Long agendamentoId) throws NotFoundException {
		return repository.findByIdAgendamento(agendamentoId).orElseThrow(() -> new NotFoundException(NEGOCIACAO_NAO_ENCONTRADA));
	}

	@Override
	public void removerNegociacao(Long idNegociacao) throws NotFoundException {
		var negociacao = buscaPorId(idNegociacao);
		repository.delete(negociacao);
	}
	
	
	

}
