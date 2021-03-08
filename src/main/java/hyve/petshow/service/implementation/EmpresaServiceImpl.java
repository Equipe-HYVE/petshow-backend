package hyve.petshow.service.implementation;

import static hyve.petshow.util.AuditoriaUtils.ATIVO;
import static hyve.petshow.util.AuditoriaUtils.INATIVO;
import static hyve.petshow.util.AuditoriaUtils.atualizaAuditoria;
import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_SERVICE;
import static hyve.petshow.util.OkHttpUtils.getRequest;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hyve.petshow.domain.Empresa;
import hyve.petshow.domain.embeddables.Endereco;
import hyve.petshow.domain.embeddables.Geolocalizacao;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.EmpresaRepository;
import hyve.petshow.service.port.EmpresaService;
import hyve.petshow.util.GeoLocUtils;

@Slf4j
@Service
public class EmpresaServiceImpl implements EmpresaService {
	private static final String EMPRESA_NAO_ENCONTRADA = "EMPRESA_NAO_ENCONTRADA";
	@Autowired
	private EmpresaRepository repository;

	@Override
	public Empresa salvarEmpresa(Empresa empresa, Long prestadorId) {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "salvarEmpresa", empresa, prestadorId);
		empresa.setAuditoria(geraAuditoriaInsercao(Optional.ofNullable(prestadorId)));
		empresa.setGeolocalizacao(geraGeolocalizacao(empresa.getEndereco()));
		return repository.save(empresa);
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
	@Override
	public Empresa buscarPorId(Long id) throws Exception {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarPorId", id);
		return repository.findById(id).orElseThrow(() -> new NotFoundException(EMPRESA_NAO_ENCONTRADA));
	}	

	@Override
	public Empresa atualizaEmpresa(Long idEmpresa, Empresa empresa) throws Exception {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "atualizaEmpresa", idEmpresa, empresa);
		var domain = buscarPorId(idEmpresa);
		domain.setEndereco(empresa.getEndereco());
		domain.setGeolocalizacao(geraGeolocalizacao(domain.getEndereco()));
		domain.setAuditoria(atualizaAuditoria(domain.getAuditoria(), ATIVO));
		return repository.save(domain);
	}

	@Override
	public Empresa desativaEmpresa(Long idEmpresa, Boolean flagAtivo) throws Exception {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "desativaEmpresa", idEmpresa, flagAtivo);
		var domain = buscarPorId(idEmpresa);
		domain.setAuditoria(atualizaAuditoria(domain.getAuditoria(), flagAtivo ? ATIVO : INATIVO));
		return repository.save(domain);
	}

}
