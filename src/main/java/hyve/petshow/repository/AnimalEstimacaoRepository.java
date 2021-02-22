package hyve.petshow.repository;

import hyve.petshow.domain.AnimalEstimacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnimalEstimacaoRepository extends JpaRepository<AnimalEstimacao, Long> {
    Page<AnimalEstimacao> findByDonoId(Long id, Pageable pageable);
    List<AnimalEstimacao> findByDonoIdAndIdIn(Long donoId, List<Long> animaisEstimacaoIds);
}
