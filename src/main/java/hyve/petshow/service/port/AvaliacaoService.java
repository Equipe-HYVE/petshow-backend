package hyve.petshow.service.port;

import hyve.petshow.domain.Avaliacao;
import hyve.petshow.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AvaliacaoService {
	Page<Avaliacao> buscarAvaliacoesPorServicoId(Long id, Pageable pageable) throws NotFoundException;

	Avaliacao adicionarAvaliacao(Avaliacao avaliacao);

	Avaliacao buscarAvaliacaoPorId(Long id) throws Exception;

	Avaliacao buscarAvaliacaoPorAgendamentoId(Long id) throws Exception;
}
