package hyve.petshow.repository;

import hyve.petshow.domain.Avaliacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
	Page<Avaliacao> findByServicoAvaliadoId(Long id, Pageable pageable);

	Optional<Avaliacao> findByAgendamentoAvaliadoId(Long id);
}
