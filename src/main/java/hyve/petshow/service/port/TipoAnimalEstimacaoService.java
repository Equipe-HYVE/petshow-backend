package hyve.petshow.service.port;

import hyve.petshow.domain.TipoAnimalEstimacao;
import hyve.petshow.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TipoAnimalEstimacaoService {
    List<TipoAnimalEstimacao> buscarTiposAnimalEstimacao() throws NotFoundException;
}
