package hyve.petshow.service.implementation;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.AnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AnimalEstimacaoRepository;
import hyve.petshow.service.port.AnimalEstimacaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.*;
import static hyve.petshow.util.LogUtils.INFO_REQUEST_SERVICE;
import static hyve.petshow.util.ProxyUtils.verificarIdentidade;

@Slf4j
@Service
public class AnimalEstimacaoServiceImpl implements AnimalEstimacaoService {
    private static final String ANIMAL_ESTIMACAO_NAO_ENCONTRADO = "ANIMAL_ESTIMACAO_NAO_ENCONTRADO";//"Animal de estimação não encontrado";
    private static final String NENHUM_ANIMAL_ESTIMACAO_ENCONTRADO = "NENHUM_ANIMAL_ESTIMACAO_ENCONTRADO";// "Nenhum animal de estimação encontrado";
    private static final String USUARIO_NAO_PROPRIETARIO_ANIMAL = "USUARIO_NAO_PROPRIETARIO_ANIMAL";//"Este animal não pertence a este usuário";

    @Autowired
    private AnimalEstimacaoRepository animalEstimacaoRepository;

    @Override
    public AnimalEstimacao adicionarAnimalEstimacao(AnimalEstimacao animalEstimacao) {
        log.info(INFO_REQUEST_SERVICE.concat("{}"), "adicionarAnimalEstimacao", animalEstimacao);
        animalEstimacao.setAuditoria(geraAuditoriaInsercao(Optional.of(animalEstimacao.getDonoId())));

        return animalEstimacaoRepository.save(animalEstimacao);
    }

    @Override
    public AnimalEstimacao buscarAnimalEstimacaoPorId(Long id) throws NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarAnimalEstimacaoPorId", id);
        return animalEstimacaoRepository.findByIdAndAuditoriaFlagAtivo(id, ATIVO).orElseThrow(
                () -> new NotFoundException(ANIMAL_ESTIMACAO_NAO_ENCONTRADO));
    }

    @Override
    public List<AnimalEstimacao> buscarAnimaisEstimacaoPorIds(Long donoId, List<Long> animaisEstimacaoIds) throws NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarAnimaisEstimacaoPorIds", donoId, animaisEstimacaoIds);
        var animaisEstimacao = animalEstimacaoRepository.findByDonoIdAndIdInAndAuditoriaFlagAtivo(donoId, animaisEstimacaoIds, ATIVO);

        if(animaisEstimacao.isEmpty()) {
            throw new NotFoundException(NENHUM_ANIMAL_ESTIMACAO_ENCONTRADO);
        }

        return animaisEstimacao;
    }

    @Override
    public Page<AnimalEstimacao> buscarAnimaisEstimacaoPorDono(Long id, Pageable pageable) throws NotFoundException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarAnimaisEstimacaoPorDono", id, pageable);
        var animaisEstimacao = animalEstimacaoRepository.findByDonoIdAndAuditoriaFlagAtivo(id, ATIVO, pageable);

        return animaisEstimacao;
    }

    @Override
    public AnimalEstimacao atualizarAnimalEstimacao(Long id, AnimalEstimacao request)
            throws NotFoundException, BusinessException {
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "atualizarAnimalEstimacao", id, request);
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
        log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "removerAnimalEstimacao", id, donoId);
        var animalEstimacao =  buscarAnimalEstimacaoPorId(id);

        if(!verificarIdentidade(animalEstimacao.getDonoId(), donoId)) {
        	throw new BusinessException(USUARIO_NAO_PROPRIETARIO_ANIMAL);
        }

        animalEstimacao.setAuditoria(atualizaAuditoria(animalEstimacao.getAuditoria(), INATIVO));

        var animalEstimacaoResponse = animalEstimacaoRepository.save(animalEstimacao);

        var sucesso = ! animalEstimacaoResponse.getAuditoria().isAtivo();
        var response = new MensagemRepresentation(id);

        response.setSucesso(sucesso);

        return response;
    }
}
