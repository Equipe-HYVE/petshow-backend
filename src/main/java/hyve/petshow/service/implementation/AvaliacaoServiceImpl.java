package hyve.petshow.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyve.petshow.domain.Avaliacao;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AvaliacaoRepository;
import hyve.petshow.service.port.AvaliacaoService;

@Service
public class AvaliacaoServiceImpl implements AvaliacaoService {
	@Autowired
	private AvaliacaoRepository repository;
	
	@Override
	public List<Avaliacao> buscarAvaliacoesPorServico(ServicoDetalhado servico) {
		return repository.findByServicoAvaliado(servico);
	}

	@Override
	public Avaliacao adicionarAvaliacao(Avaliacao avaliacao) {
		return repository.save(avaliacao);
	}

	@Override
	public Avaliacao buscarAvaliacaoPorId(Long id) throws NotFoundException {
		return repository.findById(id).orElseThrow(()-> new NotFoundException("Avaliação não encontrada"));
	}

}
