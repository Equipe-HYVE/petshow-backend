package hyve.petshow.service.port;

import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.domain.ServicoDetalhadoTipoAnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public interface ServicoDetalhadoService {
	ServicoDetalhado adicionarServicoDetalhado(ServicoDetalhado servicoDetalhado) throws BusinessException;

	Page<ServicoDetalhado> buscarServicosDetalhadosPorTipoServico(Pageable pageable, ServicoDetalhadoFilter filtragem) throws NotFoundException;

    List<ServicoDetalhado> buscarServicosDetalhadosPorTipoServico(ServicoDetalhadoFilter filtragem);

    ServicoDetalhado adicionarTipoAnimalAceito(Long id, Long prestadorId, ServicoDetalhadoTipoAnimalEstimacao servicoDetalhadoTipoAnimalEstimacao) throws BusinessException, NotFoundException;

    ServicoDetalhado atualizarTipoAnimalAceito(Long id, Long prestadorId, Integer idTipoAnimal, ServicoDetalhadoTipoAnimalEstimacao servicoDetalhadoTipoAnimalEstimacao) throws BusinessException, NotFoundException;

    void atualizarMediaAvaliacaoServicoDetalhado(Long id, Float mediaAvaliacao) throws NotFoundException;

    ServicoDetalhado atualizarServicoDetalhado(Long id, Long prestadorId, Boolean ativo) throws BusinessException, NotFoundException;
    
    ServicoDetalhado buscarPorId(Long id) throws NotFoundException;
    
    Page<ServicoDetalhado> buscarPorPrestadorId(Long prestadorId, Pageable pageable) throws NotFoundException;
    
    ServicoDetalhado buscarPorPrestadorIdEServicoId(Long prestadorId, Long servicoId) throws NotFoundException;
    
    List<ServicoDetalhado> buscarServicosDetalhadosPorIds(List<Long> idsServicos);
 }
