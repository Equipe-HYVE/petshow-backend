package hyve.petshow.repository;

import hyve.petshow.domain.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusAgendamentoRepository extends JpaRepository<StatusAgendamento, Integer> {
}
