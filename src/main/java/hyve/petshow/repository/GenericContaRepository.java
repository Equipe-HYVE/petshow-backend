package hyve.petshow.repository;

import hyve.petshow.domain.Conta;

import javax.transaction.Transactional;

@Transactional
public interface GenericContaRepository extends ContaRepository<Conta>{

}
