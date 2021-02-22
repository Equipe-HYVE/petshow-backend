package hyve.petshow.service.implementation;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Conta;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.GenericContaRepository;
import hyve.petshow.service.port.GenericContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.*;

@Service
public class ContaServiceImpl implements GenericContaService {
	private static final String CONTA_NAO_ENCONTRADA = "CONTA_NAO_ENCONTRADA";
	@Autowired
	private GenericContaRepository repository;

	@Override
	public Conta buscarPorId(Long id) throws Exception {
		return repository.findById(id).orElseThrow(() -> new NotFoundException(CONTA_NAO_ENCONTRADA));
	}

	@Override
	public MensagemRepresentation desativarConta(Long id) throws Exception {
		var cliente = buscarPorId(id);
		var mensagem = new MensagemRepresentation();

		cliente.setAuditoria(atualizaAuditoria(cliente.getAuditoria(), INATIVO));
		cliente = repository.save(cliente);

		mensagem.setId(id);
		mensagem.setSucesso(!cliente.isAtivo());
		return mensagem;
	}

	@Override
	public Optional<Conta> buscarPorEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	public Conta atualizarConta(Long id, Conta request) throws Exception {
		var conta = buscarPorId(id);

		conta.setTelefone(request.getTelefone());
		conta.setEndereco(request.getEndereco());
		conta.setAuditoria(atualizaAuditoria(conta.getAuditoria(), ATIVO));

		return repository.save(conta);
	}

}
