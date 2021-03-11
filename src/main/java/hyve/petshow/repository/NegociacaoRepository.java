package hyve.petshow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hyve.petshow.domain.Negociacao;

public interface NegociacaoRepository extends JpaRepository<Negociacao, Long> {
	Optional<Negociacao> findByIdAgendamento(Long idAgendamento);

}
