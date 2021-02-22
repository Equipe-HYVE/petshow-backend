package hyve.petshow.repository;

import hyve.petshow.domain.ServicoDetalhado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ServicoDetalhadoRepository extends JpaRepository<ServicoDetalhado, Long>, JpaSpecificationExecutor<ServicoDetalhado>{
	@Query("select s from servico_detalhado s left join fetch s.adicionais where s.id=?1")
	Optional<ServicoDetalhado> findById(Long id);

	Page<ServicoDetalhado> findByPrestadorId(Long prestadorId, Pageable pageable);
	
	Optional<ServicoDetalhado> findByIdAndPrestadorId(Long idServico, Long idPrestador);
}