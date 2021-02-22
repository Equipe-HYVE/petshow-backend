package hyve.petshow.repository;

import hyve.petshow.domain.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ClienteRepository extends ContaRepository<Cliente> {
	@Query("select c from Cliente c left join fetch c.animaisEstimacao where c.id = ?1")
	Optional<Cliente> findById(Long id);
}
