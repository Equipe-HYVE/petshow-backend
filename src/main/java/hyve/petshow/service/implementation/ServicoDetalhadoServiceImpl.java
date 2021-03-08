package hyve.petshow.service.implementation;

import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.domain.ServicoDetalhadoTipoAnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.ServicoDetalhadoRepository;
import hyve.petshow.repository.nativeQueryRepository.ServicoDetalhadoTipoAnimalEstimacaoNativeQueryRepository;
import hyve.petshow.service.port.ServicoDetalhadoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static hyve.petshow.repository.specification.ServicoDetalhadoSpecification.geraSpecification;
import static hyve.petshow.util.AuditoriaUtils.*;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_SERVICE;
import static hyve.petshow.util.NullUtils.isNotNull;
import static hyve.petshow.util.ProxyUtils.verificarIdentidade;

@Slf4j
@Service
public class ServicoDetalhadoServiceImpl implements ServicoDetalhadoService {
	private static final String SERVICO_NAO_ENCONTRADO_PARA_PRESTADOR_MENCIONADO = "SERVICO_NAO_ENCONTRADO_PARA_PRESTADOR_MENCIONADO";//"Serviço não encontrado para prestador mencionado";
	private static final String SERVICO_DETALHADO_NAO_ENCONTRADO = "SERVICO_DETALHADO_NAO_ENCONTRADO";//"Serviço detalhado não encontrado";
	private static final String NENHUM_SERVICO_DETALHADO_ENCONTRADO = "NENHUM_SERVICO_DETALHADO_ENCONTRADO";//"Nenhum serviço detalhado encontrado";
	private static final String USUARIO_NAO_PROPRIETARIO_SERVICO = "USUARIO_NAO_PROPRIETARIO_SERVICO";//"Este serviço não pertence a este usuário";
	private static final String SERVICO_DESTE_TIPO_JA_ADICIONADO = "SERVICO_DESTE_TIPO_JA_ADICIONADO"; //"Já existe um serviço deste tipo cadastrado para este prestador.";
	private static final String PRECO_JA_REGISTRADO_PARA_TIPO = "PRECO_JA_REGISTRADO_PARA_TIPO";
	private static final String PRECO_POR_TIPO_NAO_ENCONTRADO = "PRECO_POR_TIPO_NAO_ENCONTRADO";

	@Autowired
	private ServicoDetalhadoRepository repository;

	@Autowired
	private ServicoDetalhadoTipoAnimalEstimacaoNativeQueryRepository servicoDetalhadoTipoAnimalEstimacaoNativeQueryRepository;
	
	@Override
	public ServicoDetalhado adicionarServicoDetalhado(ServicoDetalhado servicoDetalhado) throws BusinessException {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "adicionarServicoDetalhado", servicoDetalhado);
		var servicosPrestador = repository.findByPrestadorId(servicoDetalhado.getPrestadorId());

		if(servicosPrestador.stream()
				.filter(servicoPrestador ->
						servicoPrestador.getAuditoria().isAtivo() &&
						servicoPrestador.getTipo().equals(servicoDetalhado.getTipo()))
				.findAny().isPresent()) {
			throw new BusinessException(SERVICO_DESTE_TIPO_JA_ADICIONADO);
		}

		servicoDetalhado.setAuditoria(geraAuditoriaInsercao(Optional.of(servicoDetalhado.getPrestadorId())));
		servicoDetalhado.setAdicionais(Optional.ofNullable(servicoDetalhado.getAdicionais())
				.map(lista -> {
					return lista.stream().map(el -> {
						el.setAuditoria(geraAuditoriaInsercao(Optional.of(servicoDetalhado.getPrestadorId())));
						return el;
					}).collect(Collectors.toList());
				}).orElse(new ArrayList<>()));
		servicoDetalhado.setTiposAnimaisAceitos(servicoDetalhado.getTiposAnimaisAceitos().stream()
			.map(tipoAnimalAceito -> {
				tipoAnimalAceito.setAuditoria(geraAuditoriaInsercao(Optional.of(servicoDetalhado.getPrestadorId())));
				return tipoAnimalAceito;
			}).collect(Collectors.toList()));
		
		return repository.save(servicoDetalhado);
	}

	@Override
	public Page<ServicoDetalhado> buscarServicosDetalhadosPorTipoServico(Pageable pageable,
																		 ServicoDetalhadoFilter filtragem) throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarServicosDetalhadosPorTipoServico", pageable, filtragem);
		var specification = geraSpecification(filtragem);

		var servicosDetalhados = repository.findAll(specification, pageable);

		return servicosDetalhados;
	}
	
	@Override
	public List<ServicoDetalhado> buscarServicosDetalhadosPorTipoServico(ServicoDetalhadoFilter filtragem) {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarServicosDetalhadosPorTipoServico", filtragem);
		var spec = geraSpecification(filtragem);
		var servicosDetalhados = repository.findAll(spec);
		return servicosDetalhados;
	}

	@Override
	public ServicoDetalhado adicionarTipoAnimalAceito(Long id, Long prestadorId, ServicoDetalhadoTipoAnimalEstimacao request)
			throws BusinessException, NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}, {}"), "adicionarTipoAnimalAceito", id, prestadorId, request);
		var servicoDetalhado = buscarPorId(id);

		if(!verificarIdentidade(servicoDetalhado.getPrestadorId(), prestadorId)) {
			throw new BusinessException(USUARIO_NAO_PROPRIETARIO_SERVICO);
		}
		var servicoDetalhadoTipoAnimalEstimacao =
				new ServicoDetalhadoTipoAnimalEstimacao(servicoDetalhado, request.getTipoAnimalEstimacao(),
						request.getPreco(), geraAuditoriaInsercao(Optional.of(prestadorId)));

		try {
			servicoDetalhadoTipoAnimalEstimacaoNativeQueryRepository.adicionar(servicoDetalhadoTipoAnimalEstimacao);
		} catch (PersistenceException e) {
			throw new BusinessException(PRECO_JA_REGISTRADO_PARA_TIPO);
		}

		return buscarPorId(id);
	}

	@Override
	public ServicoDetalhado atualizarTipoAnimalAceito(Long id, Long prestadorId, Integer idTipoAnimal,
													  ServicoDetalhadoTipoAnimalEstimacao request) throws BusinessException, NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}, {}, {}"), "atualizarTipoAnimalAceito", id, prestadorId, idTipoAnimal, request);
		var servicoDetalhado = repository.findById(id)
				.orElseThrow(() -> new NotFoundException(SERVICO_DETALHADO_NAO_ENCONTRADO));

		/*TODO: ARRUMAR ESSE NEGOCIO NAO MUITO LEGAL*/
		servicoDetalhado.setAdicionais(
				servicoDetalhado.getAdicionais().stream()
						.filter(adicional -> adicional.getAuditoria().isAtivo())
						.collect(Collectors.toList()));

		if(!verificarIdentidade(servicoDetalhado.getPrestadorId(), prestadorId)) {
			throw new BusinessException(USUARIO_NAO_PROPRIETARIO_SERVICO);
		}

		servicoDetalhado.getTiposAnimaisAceitos().stream()
				.forEach(tipoAnimalAceito -> {
					if(tipoAnimalAceito.getTipoAnimalEstimacao().getId() == idTipoAnimal){
						if(isNotNull(request.getPreco())){
							tipoAnimalAceito.setPreco(request.getPreco());
						}

						if(isNotNull(request.getAuditoria())){
							tipoAnimalAceito.setAuditoria(atualizaAuditoria(tipoAnimalAceito.getAuditoria(),
									request.getAuditoria().getFlagAtivo()));
						}
					}
				});
		servicoDetalhado.setAuditoria(atualizaAuditoria(servicoDetalhado.getAuditoria(), ATIVO));

		var response = repository.save(servicoDetalhado);

		return response;
	}

	@Override
	public void atualizarMediaAvaliacaoServicoDetalhado(Long id, Float mediaAvaliacao) throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "atualizarMediaAvaliacaoServicoDetalhado", id, mediaAvaliacao);
		var servicoDetalhado = buscarPorId(id);

		servicoDetalhado.setMediaAvaliacao(mediaAvaliacao);

		repository.save(servicoDetalhado);
	}

	@Override
	public ServicoDetalhado atualizarServicoDetalhado(Long id, Long prestadorId, Boolean ativo)
			throws BusinessException, NotFoundException{
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}, {}"), "atualizarServicoDetalhado", id, prestadorId, ativo);
		var servicoDetalhado = buscarPorId(id);

		if(!verificarIdentidade(servicoDetalhado.getPrestadorId(), prestadorId)) {
			throw new BusinessException(USUARIO_NAO_PROPRIETARIO_SERVICO);
		}

		servicoDetalhado.setAuditoria(atualizaAuditoria(servicoDetalhado.getAuditoria(), ativo ? ATIVO : INATIVO));

		var response = repository.save(servicoDetalhado);

		return response;
    }

	@Override
	public ServicoDetalhado buscarPorId(Long id) throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarPorId", id);
		var servicoDetalhado = repository.findById(id)
				.orElseThrow(() -> new NotFoundException(SERVICO_DETALHADO_NAO_ENCONTRADO));

		/*TODO: ARRUMAR ESSE NEGOCIO NAO MUITO LEGAL*/
		servicoDetalhado.setAdicionais(
				servicoDetalhado.getAdicionais().stream()
						.filter(adicional -> adicional.getAuditoria().isAtivo())
						.collect(Collectors.toList()));

		/*TODO: ARRUMAR ESSE OUTRO NEGOCIO NAO MUITO LEGAL*/
		servicoDetalhado.setTiposAnimaisAceitos(
				servicoDetalhado.getTiposAnimaisAceitos().stream()
						.filter(tipoAnimalAceito -> tipoAnimalAceito.getAuditoria().isAtivo())
						.collect(Collectors.toList()));

		return servicoDetalhado;
	}

	@Override
	public Page<ServicoDetalhado> buscarPorPrestadorId(Long prestadorId, Pageable pageable) throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarPorPrestadorId", prestadorId, pageable);
		var servicosDetalhados = repository.findByPrestadorId(prestadorId, pageable);

		if(servicosDetalhados.isEmpty()){
			throw new NotFoundException(NENHUM_SERVICO_DETALHADO_ENCONTRADO);
		}

		return servicosDetalhados;
	}

	@Override
	public ServicoDetalhado buscarPorPrestadorIdEServicoId(Long prestadorId, Long servicoId) throws NotFoundException {
		log.info(INFO_REQUEST_SERVICE.concat("{}, {}"), "buscarPorPrestadorIdEServicoId", prestadorId, servicoId);
		var servicoDetalhado = repository.findByIdAndPrestadorId(servicoId, prestadorId)
				.orElseThrow(() -> new NotFoundException(SERVICO_NAO_ENCONTRADO_PARA_PRESTADOR_MENCIONADO));

		/*TODO: ARRUMAR ESSE NEGOCIO NAO MUITO LEGAL*/
		servicoDetalhado.setAdicionais(
				servicoDetalhado.getAdicionais().stream()
						.filter(adicional -> adicional.getAuditoria().isAtivo())
						.collect(Collectors.toList()));

		/*TODO: ARRUMAR ESSE OUTRO NEGOCIO NAO MUITO LEGAL*/
		servicoDetalhado.setTiposAnimaisAceitos(
				servicoDetalhado.getTiposAnimaisAceitos().stream()
						.filter(tipoAnimalAceito -> tipoAnimalAceito.getAuditoria().isAtivo())
						.collect(Collectors.toList()));

		return servicoDetalhado;
	}

	@Override
	public List<ServicoDetalhado> buscarServicosDetalhadosPorIds(List<Long> idsServicos) {
		log.info(INFO_REQUEST_SERVICE.concat("{}"), "buscarServicosDetalhadosPorIds", idsServicos);
		var servicosDetalhados =  repository.findAllById(idsServicos);

		/*TODO: ARRUMAR ESSES NEGOCIOS NAO MUITO LEGAIS*/
		servicosDetalhados.stream()
				.forEach(servicoDetalhado -> {
					servicoDetalhado.setAdicionais(
							servicoDetalhado.getAdicionais().stream()
									.filter(adicional -> adicional.getAuditoria().isAtivo())
									.collect(Collectors.toList()));
					servicoDetalhado.setTiposAnimaisAceitos(
							servicoDetalhado.getTiposAnimaisAceitos().stream()
									.filter(tipoAnimalAceito -> tipoAnimalAceito.getAuditoria().isAtivo())
									.collect(Collectors.toList()));
				});

		return servicosDetalhados;
	}
}
