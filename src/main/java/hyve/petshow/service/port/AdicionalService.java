package hyve.petshow.service.port;

import hyve.petshow.domain.Adicional;

import java.util.List;

public interface AdicionalService {
	List<Adicional> buscarAtivosPorServicoDetalhado(Long idServico) throws Exception;
	List<Adicional> buscarPorServicoDetalhado(Long idServico) throws Exception;
	Adicional buscarPorId(Long idAdicional) throws Exception;
	List<Adicional> buscarAdicionaisPorIds(Long servicoDetalhadoId, List<Long> adicionaisIds) throws Exception;
	Adicional criarAdicional(Adicional adicional, Long prestadorId);
	Adicional atualizarAdicional(Long idAdicional, Adicional adicional) throws Exception;
	Adicional desativarAdicional(Long idAdicional, Long idServico, Boolean ativo) throws Exception;
	List<Adicional> criarAdicionais(List<Adicional> adicionais, Long prestadorId);
}
