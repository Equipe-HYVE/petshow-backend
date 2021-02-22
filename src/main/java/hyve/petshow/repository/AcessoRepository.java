package hyve.petshow.repository;

import hyve.petshow.domain.Conta;
import hyve.petshow.domain.embeddables.Login;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcessoRepository extends ContaRepository<Conta>{
    Optional<Conta> findByLogin(Login login);
}
