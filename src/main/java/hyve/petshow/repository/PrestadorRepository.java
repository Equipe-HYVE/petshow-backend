package hyve.petshow.repository;

import hyve.petshow.domain.Prestador;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface PrestadorRepository extends ContaRepository<Prestador> {
    @Query("select p from Prestador p left join fetch p.servicosPrestados where p.id = ?1")
    Optional<Prestador> findById(Long id);
}
