package hyve.petshow.service.port;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.AnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface AnimalEstimacaoService {
    AnimalEstimacao adicionarAnimalEstimacao(AnimalEstimacao animalEstimacao);

    AnimalEstimacao buscarAnimalEstimacaoPorId(Long id) throws NotFoundException;

    List<AnimalEstimacao> buscarAnimaisEstimacaoPorIds(Long donoId, List<Long> animaisEstimacaoIds) throws NotFoundException;

    Page<AnimalEstimacao> buscarAnimaisEstimacaoPorDono(Long id, Pageable pageable) throws NotFoundException;

    AnimalEstimacao atualizarAnimalEstimacao(Long id, AnimalEstimacao animalEstimacao) throws NotFoundException, BusinessException;

    MensagemRepresentation removerAnimalEstimacao(Long id, Long donoId) throws BusinessException, NotFoundException;
}
