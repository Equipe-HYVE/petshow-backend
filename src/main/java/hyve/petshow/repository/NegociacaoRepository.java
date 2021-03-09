package hyve.petshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hyve.petshow.domain.Negociacao;

public interface NegociacaoRepository extends JpaRepository<Negociacao, Long>{

}
