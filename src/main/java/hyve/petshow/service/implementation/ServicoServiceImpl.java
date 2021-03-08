package hyve.petshow.service.implementation;

import hyve.petshow.domain.Servico;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.ServicoRepository;
import static hyve.petshow.repository.specification.ServicoSpecification.geraSpecification;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_SERVICE;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_SERVICE_EMPTY;

import hyve.petshow.service.port.ServicoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ServicoServiceImpl extends TipoService<Servico> implements ServicoService {
    private static final String NENHUM_SERVICO_ENCONTRADO = "NENHUM_SERVICO_ENCONTRADO";//"Nenhum serviço encontrado";

	public ServicoServiceImpl() {
		super(NENHUM_SERVICO_ENCONTRADO);
	}

    @Autowired
    private ServicoRepository repository;
    
    @Override
    public List<Servico> buscarServicos() throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE_EMPTY, "buscarServicos");
		return buscarTodos();
    }

	@Override
	public List<Servico> buscarLista() {
		log.info(INFO_REQUEST_SERVICE_EMPTY, "buscarLista");
    	return repository.findAll();
	}

	@Override
	public List<Servico> buscarServicosPresentesEmEstado(String cidade, String estado) throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarServicosPresentesEmEstado", cidade, estado);
		var findAll = repository.findAll(geraSpecification(cidade, estado));
		return validaLista(findAll);
	}
	
	
}

