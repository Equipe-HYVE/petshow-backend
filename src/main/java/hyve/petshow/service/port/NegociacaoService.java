package hyve.petshow.service.port;

import org.springframework.stereotype.Service;

import hyve.petshow.domain.Negociacao;

@Service
public interface NegociacaoService {
	Negociacao criaNegociacao(Long idAgendamento, Negociacao negociacao);

	Negociacao atualizaNegociacao(Long idNegociacao, Negociacao negociacao);

	Negociacao cancelaNegociacao(Long idNegociacao);
}
