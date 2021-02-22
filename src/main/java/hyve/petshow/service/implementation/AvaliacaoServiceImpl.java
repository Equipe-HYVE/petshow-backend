package hyve.petshow.service.implementation;

import hyve.petshow.domain.Avaliacao;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AvaliacaoRepository;
import hyve.petshow.service.port.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;

@Service
public class AvaliacaoServiceImpl implements AvaliacaoService {
	private static final String AVALIACAO_NAO_ENCONTRADA = "AVALIACAO_NAO_ENCONTRADA";//Avaliação não encontrada
	private static final String NENHUMA_AVALIACAO_ENCONTRADA = "NENHUMA_AVALIACAO_ENCONTRADA";//Nenhuma avaliação encontrada

	@Autowired
	private AvaliacaoRepository repository;
	
	@Override
	public Page<Avaliacao> buscarAvaliacoesPorServicoId(Long id, Pageable pageable) throws NotFoundException {
		var avaliacoes = repository.findByServicoAvaliadoId(id, pageable);

		if(avaliacoes.isEmpty()){
			throw new NotFoundException(NENHUMA_AVALIACAO_ENCONTRADA);
		}

		return avaliacoes;
	}

	@Override
	public Avaliacao adicionarAvaliacao(Avaliacao avaliacao) {
		avaliacao.setAuditoria(geraAuditoriaInsercao(Optional.of(avaliacao.getCliente().getId())));

		return repository.save(avaliacao);
	}

	@Override
	public Avaliacao buscarAvaliacaoPorId(Long id) throws NotFoundException {
		return repository.findById(id)
				.orElseThrow(()-> new NotFoundException(AVALIACAO_NAO_ENCONTRADA));
	}

	@Override
	public Avaliacao buscarAvaliacaoPorAgendamentoId(Long id) throws Exception {
		return repository.findByAgendamentoAvaliadoId(id)
				.orElseThrow(()-> new NotFoundException(AVALIACAO_NAO_ENCONTRADA));
	}
}
