package hyve.petshow.service.port;

import java.util.List;

import hyve.petshow.domain.Adicional;
import hyve.petshow.domain.AnimalEstimacao;
import hyve.petshow.exceptions.NotFoundException;

public interface AdicionalService {
	List<Adicional> buscarPorServicoDetalhado(Long idServico) throws Exception;
	Adicional buscarPorId(Long idAdicional) throws Exception;
	List<Adicional> buscarAdicionaisPorIds(Long servicoDetalhadoId, List<Long> adicionaisIds) throws Exception;
	Adicional criarAdicional(Adicional adicional, Long prestadorId);
	Adicional atualizarAdicional(Long idAdicional, Adicional adicional) throws Exception;
	Boolean desativarAdicional(Long idAdicional, Long idServico) throws Exception;
	List<Adicional> criarAdicionais(List<Adicional> adicionais, Long prestadorId);
}
