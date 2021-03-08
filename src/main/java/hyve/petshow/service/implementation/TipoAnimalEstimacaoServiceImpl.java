package hyve.petshow.service.implementation;

import hyve.petshow.domain.TipoAnimalEstimacao;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.TipoAnimalEstimacaoRepository;
import hyve.petshow.service.port.TipoAnimalEstimacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_SERVICE;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_SERVICE_EMPTY;

@Slf4j
@Service
public class TipoAnimalEstimacaoServiceImpl extends TipoService<TipoAnimalEstimacao> implements TipoAnimalEstimacaoService {
    private static final String NENHUM_TIPO_ANIMAL_ENCONTRADO = "NENHUM_TIPO_ANIMAL_ENCONTRADO";//Nenhum tipo animal de estimação encontrado

	public TipoAnimalEstimacaoServiceImpl() {
    	super(NENHUM_TIPO_ANIMAL_ENCONTRADO);
    }
    
    @Autowired
    private TipoAnimalEstimacaoRepository repository;

    @Override
    public List<TipoAnimalEstimacao> buscarTiposAnimalEstimacao() throws NotFoundException {
        log.info(INFO_REQUEST_SERVICE_EMPTY, "buscarTiposAnimalEstimacao");
        return buscarTodos();
    }

    @Override
    public TipoAnimalEstimacao buscarTipoAnimalEstimacaoPorId(Integer id) throws NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarTipoAnimalEstimacaoPorId", id);
        var tipoAnimalEstimacao = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(NENHUM_TIPO_ANIMAL_ENCONTRADO));

        return tipoAnimalEstimacao;
    }

    @Override
	public List<TipoAnimalEstimacao> buscarLista() {
        log.info(INFO_REQUEST_SERVICE_EMPTY, "buscarLista");
        return repository.findAll();
	}
}
