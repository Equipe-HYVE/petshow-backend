package hyve.petshow.repository;

import hyve.petshow.domain.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ContaRepository<T extends Conta> extends JpaRepository<T, Long> {
	@Query("select c from conta c where c.login.email = ?1")
	Optional<T> findByEmail(String email);
}
