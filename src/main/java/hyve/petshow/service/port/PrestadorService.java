package hyve.petshow.service.port;

import hyve.petshow.domain.Prestador;
import org.springframework.stereotype.Service;

@Service
public interface PrestadorService extends ContaService<Prestador> {
	Prestador atualizarConta(Long id, Prestador request, String ativo) throws Exception;
}
