package hyve.petshow.service.implementation;

import hyve.petshow.domain.Avaliacao;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AvaliacaoRepository;
import hyve.petshow.service.port.AvaliacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_SERVICE;

@Slf4j
@Service
public class AvaliacaoServiceImpl implements AvaliacaoService {
	private static final String AVALIACAO_NAO_ENCONTRADA = "AVALIACAO_NAO_ENCONTRADA";//Avaliação não encontrada
	private static final String NENHUMA_AVALIACAO_ENCONTRADA = "NENHUMA_AVALIACAO_ENCONTRADA";//Nenhuma avaliação encontrada

	@Autowired
	private AvaliacaoRepository repository;
	
	@Override
	public Page<Avaliacao> buscarAvaliacoesPorServicoId(Long id, Pageable pageable) throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarAvaliacoesPorServicoId", id, pageable);
		var avaliacoes = repository.findByServicoAvaliadoId(id, pageable);

		if(avaliacoes.isEmpty()){
			throw new NotFoundException(NENHUMA_AVALIACAO_ENCONTRADA);
		}

		return avaliacoes;
	}

	@Override
	public Avaliacao adicionarAvaliacao(Avaliacao avaliacao) {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "adicionarAvaliacao", avaliacao);
		avaliacao.setAuditoria(geraAuditoriaInsercao(Optional.of(avaliacao.getCliente().getId())));

		return repository.save(avaliacao);
	}

	@Override
	public Avaliacao buscarAvaliacaoPorId(Long id) throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarAvaliacaoPorId", id);
		return repository.findById(id)
				.orElseThrow(()-> new NotFoundException(AVALIACAO_NAO_ENCONTRADA));
	}

	@Override
	public Avaliacao buscarAvaliacaoPorAgendamentoId(Long id) throws Exception {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarAvaliacaoPorAgendamentoId", id);
		return repository.findByAgendamentoAvaliadoId(id)
				.orElseThrow(()-> new NotFoundException(AVALIACAO_NAO_ENCONTRADA));
	}

	@Override
	public Float buscarMediaAvaliacaoPorServicoDetalhadoId(Long servicoDetalhadoId) {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarMediaAvaliacaoPorServicoDetalhadoId", servicoDetalhadoId);
		return repository.findMediaAvaliacaoByServicoDetalhado(servicoDetalhadoId);
	}
}
