package hyve.petshow.service.port;

import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public interface ServicoDetalhadoService {
	ServicoDetalhado adicionarServicoDetalhado(ServicoDetalhado servicoDetalhado);

	Page<ServicoDetalhado> buscarServicosDetalhadosPorTipoServico(Pageable pageable, ServicoDetalhadoFilter filtragem) throws NotFoundException;
	
	ServicoDetalhado atualizarServicoDetalhado(Long id, Long prestadorId, ServicoDetalhado servicoDetalhadoRequest) throws BusinessException, NotFoundException;

    MensagemRepresentation removerServicoDetalhado(Long id, Long prestadorId) throws BusinessException, NotFoundException;
    
    ServicoDetalhado buscarPorId(Long id) throws NotFoundException;
    
    Page<ServicoDetalhado> buscarPorPrestadorId(Long prestadorId, Pageable pageable) throws NotFoundException;
    
    ServicoDetalhado buscarPorPrestadorIdEServicoId(Long prestadorId, Long servicoId) throws NotFoundException;
    
    List<ServicoDetalhado> buscarServicosDetalhadosPorIds(List<Long> idsServicos);
 }
