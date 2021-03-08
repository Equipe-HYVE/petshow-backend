package hyve.petshow.service.implementation;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Conta;
import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.embeddables.Geolocalizacao;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.GenericContaRepository;
import hyve.petshow.service.port.GenericContaService;
import hyve.petshow.util.GeoLocUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.*;
import static hyve.petshow.util.LogUtils.INFO_REQUEST_SERVICE;
import static hyve.petshow.util.OkHttpUtils.getRequest;

@Slf4j
@Service
public class ContaServiceImpl implements GenericContaService {
	private static final String CONTA_NAO_ENCONTRADA = "CONTA_NAO_ENCONTRADA";
	@Autowired
	private GenericContaRepository repository;

	@Override
	public Conta buscarPorId(Long id) throws Exception {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarPorId", id);
		return repository.findById(id).orElseThrow(() -> new NotFoundException(CONTA_NAO_ENCONTRADA));
	}

	@Override
	public MensagemRepresentation desativarConta(Long id) throws Exception {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "desativarConta", id);
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
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarPorEmail", email);
		return repository.findByEmail(email);
	}

	@Override
	public Conta atualizarConta(Long id, Conta request) throws Exception {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "atualizarConta", id, request);
		var conta = buscarPorId(id);

		conta.setTelefone(request.getTelefone());
		conta.setEndereco(request.getEndereco());
		conta.setGeolocalizacao(geraGeolocalizacao(conta.getEndereco()));
		conta.setAuditoria(atualizaAuditoria(conta.getAuditoria(), ATIVO));

		return repository.save(conta);
	}

	private Geolocalizacao geraGeolocalizacao(Endereco endereco) {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "geraGeolocalizacao", endereco);
		var geolocalizacao = new Geolocalizacao();
    	try {
    		var url = GeoLocUtils.geraUrl(endereco);
        	var response = getRequest(url);
        	var geoloc = GeoLocUtils.mapeiaJson(response);
        	geolocalizacao.setGeolocLatitude(geoloc.getLat());
        	geolocalizacao.setGeolocLongitude(geoloc.getLon());
        	return geolocalizacao;
    	} catch (Exception e) {
    		return geolocalizacao;
    	}    	
	}
}
