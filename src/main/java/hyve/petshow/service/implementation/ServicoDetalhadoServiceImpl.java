package hyve.petshow.service.implementation;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.ServicoDetalhadoRepository;
import hyve.petshow.service.port.ServicoDetalhadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static hyve.petshow.util.ProxyUtils.verificarIdentidade;

//import hyve.petshow.controller.representation.ServicoDetalhadoResponseRepresentation;

@Service
public class ServicoDetalhadoServiceImpl implements ServicoDetalhadoService {
	private static final String SERVICO_NAO_ENCONTRADO_PARA_PRESTADOR_MENCIONADO = "Serviço não encontrado para prestador mencionado";
	private final String SERVICO_DETALHADO_NAO_ENCONTRADO = "Serviço detalhado não encontrado";
	private final String NENHUM_SERVICO_DETALHADO_ENCONTRADO = "Nenhum serviço detalhado encontrado";
	private final String USUARIO_NAO_PROPRIETARIO = "Este serviço não pertence a este usuário";

	@Autowired
	private ServicoDetalhadoRepository repository;
	
	@Override
	public ServicoDetalhado adicionarServicoDetalhado(ServicoDetalhado servicoDetalhado) {
		return repository.save(servicoDetalhado);
	}

	@Override
	public List<ServicoDetalhado> buscarServicosDetalhadosPorTipoServico(Integer id) throws NotFoundException {
		var servicsoDetalhados = repository.findByTipo(id);

		if(servicsoDetalhados.isEmpty()){
			throw new NotFoundException(NENHUM_SERVICO_DETALHADO_ENCONTRADO);
		}

		return servicsoDetalhados;
	}

	@Override
	public ServicoDetalhado atualizarServicoDetalhado(Long id, ServicoDetalhado request)
			throws BusinessException, NotFoundException {
		var servicoDetalhado = buscarPorId(id);

		if(verificarIdentidade(servicoDetalhado.getPrestadorId(), request.getPrestadorId())){
			servicoDetalhado.setPreco(request.getPreco());
			var response = repository.save(servicoDetalhado);
			return response;
		} else {
			throw new BusinessException(USUARIO_NAO_PROPRIETARIO);
		}
	}

	@Override
	public MensagemRepresentation removerServicoDetalhado(Long id, Long prestadorId)
			throws BusinessException, NotFoundException{
		var servicoDetalhado = buscarPorId(id);

		if(verificarIdentidade(servicoDetalhado.getPrestadorId(), prestadorId)){		
			
			repository.deleteById(id);
			var sucesso = !repository.existsById(id);
			var response = new MensagemRepresentation(id);
			response.setSucesso(sucesso);
			return response;
		} else {
			throw new BusinessException(USUARIO_NAO_PROPRIETARIO);
		}
    }

	@Override
	public ServicoDetalhado buscarPorId(Long id) throws NotFoundException {
		return repository.findById(id)
				.orElseThrow(() -> new NotFoundException(SERVICO_DETALHADO_NAO_ENCONTRADO));
	}

	@Override
	public List<ServicoDetalhado> buscarPorPrestadorId(Long prestadorId) throws NotFoundException {
		var servicosDetalhados = repository.findByPrestadorId(prestadorId);

		if(servicosDetalhados.isEmpty()){
			throw new NotFoundException(NENHUM_SERVICO_DETALHADO_ENCONTRADO);
		}

		return servicosDetalhados;
	}

	@Override
	public ServicoDetalhado buscarPorPrestadorEId(Long prestadorId, Long servicoId) throws NotFoundException {
		return repository.findByIdAndPrestadorId(servicoId, prestadorId).orElseThrow(() -> new NotFoundException(SERVICO_NAO_ENCONTRADO_PARA_PRESTADOR_MENCIONADO));
	}
}
