package hyve.petshow.service.port;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Conta;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ContaService<T extends Conta> {
	T buscarPorId(Long id) throws Exception;

	MensagemRepresentation desativarConta(Long id) throws Exception;

	Optional<T> buscarPorEmail(String email);

	T atualizarConta(Long id, T conta) throws Exception;
}
