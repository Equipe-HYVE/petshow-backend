package hyve.petshow.repository;

import hyve.petshow.domain.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    @Query("select a from agendamento a where a.cliente.id = ?1")
    Page<Agendamento> findByClienteId(Long id, Pageable pageable);

    @Query("select a from agendamento a where a.prestador.id = ?1")
    Page<Agendamento> findByPrestadorId(Long id, Pageable pageable);
}
