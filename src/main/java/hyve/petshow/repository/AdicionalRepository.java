package hyve.petshow.repository;

import hyve.petshow.domain.Adicional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdicionalRepository extends JpaRepository<Adicional, Long> {
	List<Adicional> findByServicoDetalhadoIdAndAuditoriaFlagAtivo(Long idServico, String flagAtivo);
	List<Adicional> findByServicoDetalhadoId(Long idServico);
	List<Adicional> findByServicoDetalhadoIdAndIdInAndAuditoriaFlagAtivo(Long servicoDetalhadoId, List<Long> adicionaisIds, String flagAtivo);
}
