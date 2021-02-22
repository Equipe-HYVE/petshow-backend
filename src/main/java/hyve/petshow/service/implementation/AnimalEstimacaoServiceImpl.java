package hyve.petshow.service.implementation;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.AnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AnimalEstimacaoRepository;
import hyve.petshow.service.port.AnimalEstimacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.*;
import static hyve.petshow.util.ProxyUtils.verificarIdentidade;

@Service
public class AnimalEstimacaoServiceImpl implements AnimalEstimacaoService {
    private static final String ANIMAL_ESTIMACAO_NAO_ENCONTRADO = "ANIMAL_ESTIMACAO_NAO_ENCONTRADO";//"Animal de estimação não encontrado";
    private static final String NENHUM_ANIMAL_ESTIMACAO_ENCONTRADO = "NENHUM_ANIMAL_ESTIMACAO_ENCONTRADO";// "Nenhum animal de estimação encontrado";
    private static final String USUARIO_NAO_PROPRIETARIO_ANIMAL = "USUARIO_NAO_PROPRIETARIO_ANIMAL";//"Este animal não pertence a este usuário";

    @Autowired
    private AnimalEstimacaoRepository animalEstimacaoRepository;

    @Override
    public AnimalEstimacao adicionarAnimalEstimacao(AnimalEstimacao animalEstimacao) {
        animalEstimacao.setAuditoria(geraAuditoriaInsercao(Optional.of(animalEstimacao.getDonoId())));

        return animalEstimacaoRepository.save(animalEstimacao);
    }

    @Override
    public AnimalEstimacao buscarAnimalEstimacaoPorId(Long id) throws NotFoundException {
        return animalEstimacaoRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ANIMAL_ESTIMACAO_NAO_ENCONTRADO));
    }

    @Override
    public List<AnimalEstimacao> buscarAnimaisEstimacaoPorIds(Long donoId, List<Long> animaisEstimacaoIds) throws NotFoundException {
        var animaisEstimacao = animalEstimacaoRepository.findByDonoIdAndIdIn(donoId, animaisEstimacaoIds);

        if(animaisEstimacao.isEmpty()) {
            throw new NotFoundException(NENHUM_ANIMAL_ESTIMACAO_ENCONTRADO);
        }

        return animaisEstimacao;
    }

    @Override
    public Page<AnimalEstimacao> buscarAnimaisEstimacaoPorDono(Long id, Pageable pageable) throws NotFoundException {
        var animaisEstimacao = animalEstimacaoRepository.findByDonoId(id, pageable);

        if(animaisEstimacao.isEmpty()) {
            throw new NotFoundException(NENHUM_ANIMAL_ESTIMACAO_ENCONTRADO);
        }

        return animaisEstimacao;
    }

    @Override
    public AnimalEstimacao atualizarAnimalEstimacao(Long id, AnimalEstimacao request)
            throws NotFoundException, BusinessException {
        var animalEstimacao = buscarAnimalEstimacaoPorId(id);
        
        if (!verificarIdentidade(animalEstimacao.getDonoId(), request.getDonoId())) {
        	throw new BusinessException(USUARIO_NAO_PROPRIETARIO_ANIMAL);
        }
        animalEstimacao.setNome(request.getNome());
        animalEstimacao.setTipo(request.getTipo());
        animalEstimacao.setFoto(request.getFoto());
        animalEstimacao.setAuditoria(atualizaAuditoria(animalEstimacao.getAuditoria(), ATIVO));

        var response = animalEstimacaoRepository.save(animalEstimacao);

        return response;
    }

    @Override
    public MensagemRepresentation removerAnimalEstimacao(Long id, Long donoId)
            throws BusinessException, NotFoundException {
        var animalEstimacao =  buscarAnimalEstimacaoPorId(id);

        if(!verificarIdentidade(animalEstimacao.getDonoId(), donoId)) {
        	throw new BusinessException(USUARIO_NAO_PROPRIETARIO_ANIMAL);
        }
        
        animalEstimacaoRepository.deleteById(id);

        var sucesso = !animalEstimacaoRepository.existsById(id);
        var response = new MensagemRepresentation(id);

        response.setSucesso(sucesso);

        return response;
    }
}
