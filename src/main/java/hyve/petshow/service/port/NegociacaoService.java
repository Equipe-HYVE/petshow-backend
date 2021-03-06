package hyve.petshow.service.port;

import org.springframework.stereotype.Service;

import hyve.petshow.domain.Negociacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;

@Service
public interface NegociacaoService {
	Negociacao criaNegociacao(Long idAgendamento, Long idDono, Negociacao negociacao) throws BusinessException, NotFoundException;

	Negociacao atualizaNegociacao(Long idNegociacao, Negociacao negociacao) throws BusinessException, NotFoundException;

	Negociacao cancelaNegociacao(Long idNegociacao) throws BusinessException, NotFoundException;
	
	Negociacao buscaPorId(Long idNegociacao) throws NotFoundException;
}
