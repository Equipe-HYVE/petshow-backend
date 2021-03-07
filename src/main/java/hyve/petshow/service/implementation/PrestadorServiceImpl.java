package hyve.petshow.service.implementation;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Prestador;
import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.embeddables.Geolocalizacao;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.PrestadorRepository;
import hyve.petshow.service.port.PrestadorService;
import hyve.petshow.util.GeoLocUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.*;
import static hyve.petshow.util.OkHttpUtils.getRequest;

@Service
public class PrestadorServiceImpl implements PrestadorService {
    private static final String CONTA_NAO_ENCONTRADA = "CONTA_NAO_ENCONTRADA";//"Conta não encontrada";

    @Autowired
    private PrestadorRepository repository;

    @Override
    public Prestador buscarPorId(Long id) throws Exception {
        var prestador = repository.findById(id).orElseThrow(
                () -> new NotFoundException(CONTA_NAO_ENCONTRADA));

        return prestador;
    }

    @Override
    public Prestador atualizarConta(Long id, Prestador request) throws Exception {
        var prestador = buscarPorId(id);

        prestador.setTelefone(request.getTelefone());
        prestador.setEndereco(request.getEndereco());
        prestador.setGeolocalizacao(geraGeolocalizacao(prestador.getEndereco()));
        prestador.setEmpresa(Optional.ofNullable(request.getEmpresa()).orElse(null));
        prestador.setAuditoria(atualizaAuditoria(prestador.getAuditoria(), ATIVO));
        
        return repository.save(prestador);
    }

    private Geolocalizacao geraGeolocalizacao(Endereco endereco) {
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
    
    @Override
    public MensagemRepresentation desativarConta(Long id) throws Exception {
        var conta = buscarPorId(id);
        var mensagem = new MensagemRepresentation();

        conta.setAuditoria(atualizaAuditoria(conta.getAuditoria(), INATIVO));
        conta = repository.save(conta);

        mensagem.setId(id);
        mensagem.setSucesso(conta.getAuditoria().getFlagAtivo().equals(INATIVO));

        return mensagem;
    }

    @Override
    public Optional<Prestador> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}
