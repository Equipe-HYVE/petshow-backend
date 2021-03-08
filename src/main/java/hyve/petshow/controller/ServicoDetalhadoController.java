package hyve.petshow.controller;

import hyve.petshow.controller.converter.ServicoDetalhadoConverter;
import hyve.petshow.controller.converter.ServicoDetalhadoTipoAnimalEstimacaoConverter;
import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.controller.representation.*;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.facade.AvaliacaoFacade;
import hyve.petshow.facade.ServicoDetalhadoFacade;
import hyve.petshow.service.port.ServicoDetalhadoService;
import hyve.petshow.util.ComparacaoUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_CONTROLLER_BODY_MESSAGE;
import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE;
import static hyve.petshow.util.PagingAndSortingUtils.geraPageable;

@Slf4j
@RestController
@RequestMapping
@OpenAPIDefinition(info = @Info(title = "API servico detalhado", description = "API para CRUD de servico detalhado"))
public class ServicoDetalhadoController {
	@Autowired
	private ServicoDetalhadoService service;
	@Autowired
	private ServicoDetalhadoConverter converter;
	@Autowired
	private AvaliacaoFacade avaliacaoFacade;
	@Autowired
	private ServicoDetalhadoFacade servicoDetalhadoFacade;
	@Autowired
	private ServicoDetalhadoTipoAnimalEstimacaoConverter servicoDetalhadoTipoAnimalEstimacaoConverter;

	@Operation(summary = "Busca todos os serviços detalhados por prestador.")
	@GetMapping("/prestador/{prestadorId}/servico-detalhado")
	public ResponseEntity<Page<ServicoDetalhadoRepresentation>> buscarServicosDetalhadosPorPrestador(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long prestadorId,
			@Parameter(description = "Número da página")
			@RequestParam("pagina") Integer pagina,
			@Parameter(description = "Número de itens")
			@RequestParam("quantidadeItens") Integer quantidadeItens)
			throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/prestador/{}/servico-detalhado?pagina={}&quantidadeItens={}",
				prestadorId, pagina, quantidadeItens);
		var servico = service.buscarPorPrestadorId(prestadorId, geraPageable(pagina, quantidadeItens));
		var representation = converter.toRepresentationPage(servico);

		return ResponseEntity.ok(representation);
	}

	@Operation(summary = "Busca serviços detalhados por tipo de serviço.")
	@PostMapping("/servico-detalhado/filtro")
	public ResponseEntity<Page<ServicoDetalhadoRepresentation>> buscarServicosDetalhadosPorTipoServico(
			@Parameter(description = "Número da página")
			@RequestParam("pagina") Integer pagina,
			@Parameter(description = "Número de itens")
			@RequestParam("quantidadeItens") Integer quantidadeItens,
			@Parameter(description = "Informações relacionadas a filtragem")
			@RequestBody ServicoDetalhadoFilter filtragem)
			throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/servico-detalhado/filtro?pagina={}&quantidadeItens={}",
				filtragem, pagina, quantidadeItens);
		var servicosDetalhados = servicoDetalhadoFacade
				.buscarServicosDetalhadosPorTipoServico(geraPageable(pagina, quantidadeItens), filtragem);
		var response = ResponseEntity.ok(servicosDetalhados);

		return response;
	}
	
	@Operation(summary = "Busca serviços detalhados por geolocalizacao")
	@PostMapping("/servico-detalhado/geoloc")
	public ResponseEntity<List<ServicoDetalhadoRepresentation>> buscaGeolocalizacao(
			@Parameter(description = "Informações relacionadas a filtragem")
			@RequestBody ServicoDetalhadoFilter filtragem) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/servico-detalhado/geoloc", filtragem);
		var servicosDetalhados = servicoDetalhadoFacade.buscarServicosDetalhadosPorTipoServico(filtragem);
		return ResponseEntity.ok(servicosDetalhados);
	}

	@Operation(summary = "Busca avaliações por serviço detalhado.")
	@GetMapping("/servico-detalhado/{id}/avaliacoes")
	public ResponseEntity<Page<AvaliacaoRepresentation>> buscarAvaliacoesPorServicoDetalhado(
			@Parameter(description = "Id do serviço detalhado.")
			@PathVariable Long id,
			@Parameter(description = "Número da página")
			@RequestParam("pagina") Integer pagina,
			@Parameter(description = "Número de itens")
			@RequestParam("quantidadeItens") Integer quantidadeItens)
			throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/servico-detalhado/{}/avaliacoes?pagina={}&quantidadeItens={}",
				id, pagina, quantidadeItens);
		var avaliacoes = avaliacaoFacade.buscarAvaliacaoPorServico(id, geraPageable(pagina, quantidadeItens));

		return ResponseEntity.ok(avaliacoes);
	}

	@Operation(summary = "Adiciona serviço detalhado para prestador.")
	@PostMapping("/prestador/{idPrestador}/servico-detalhado")
	public ResponseEntity<ServicoDetalhadoRepresentation> adicionarServicoDetalhado(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long idPrestador,
			@Parameter(description = "Serviço que será inserido.")
			@RequestBody ServicoDetalhadoRepresentation request) throws BusinessException {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/prestador/{}/servico-detalhado", request, idPrestador);
		var servico = converter.toDomain(request);
		servico.setPrestadorId(idPrestador);
		servico = service.adicionarServicoDetalhado(servico);
		var representation = converter.toRepresentation(servico);

		return ResponseEntity.status(HttpStatus.CREATED).body(representation);
	}

	@Operation(summary = "Deleta serviço detalhado por prestador e pelo próprio id.")
	@PatchMapping("/prestador/{prestadorId}/servico-detalhado/{id}")
	public ResponseEntity<ServicoDetalhadoRepresentation> atualizarServicoDetalhado(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long prestadorId,
			@Parameter(description = "Id do serviço detalhado.")
			@PathVariable Long id,
			@Parameter(description = "Status novo do serviço detalhado")
			@RequestParam Boolean ativo) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/prestador/{}/servico-detalhado/{}?ativo={}", prestadorId, id, ativo);
		var response = service.atualizarServicoDetalhado(id, prestadorId, ativo);
		var representation = converter.toRepresentation(response);

		return ResponseEntity.ok(representation);
	}

	@Operation(summary = "Busca serviço detalhado.")
	@GetMapping("/prestador/{prestadorId}/servico-detalhado/{servicoId}")
	public ResponseEntity<ServicoDetalhadoRepresentation> buscarPorPrestadorIdEServicoId(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long prestadorId,
			@Parameter(description = "Id do serviço detalhado.")
			@PathVariable Long servicoId) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/prestador/{}/servico-detalhado/{}", prestadorId, servicoId);
		var servicoDetalhado = servicoDetalhadoFacade.buscarPorPrestadorIdEServicoId(prestadorId, servicoId);

		return ResponseEntity.ok(servicoDetalhado);
	}

	@Operation(summary = "Adiciona novo tipo de animal aceito para serviço detalhado.")
	@PostMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}/tipoAnimalAceito/tipoAnimal/{idTipoAnimal}")
	public ResponseEntity<ServicoDetalhadoRepresentation> adicionarTipoAnimalAceito(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do serviço detalhado.")
			@PathVariable Long idServico,
			@Parameter(description = "Id do tipo do animal.")
			@PathVariable Integer idTipoAnimal,
			@Parameter(description = "Serviço detalhado a ser atualizado")
			@RequestBody PrecoPorTipoRepresentation request)
			throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/prestador/{}/servico-detalhado/{}/tipoAnimalAceito/tipoAnimal/{}",
				request, idPrestador, idServico, idTipoAnimal);
		var servico = servicoDetalhadoFacade.adicionarTipoAnimalAceito(idServico, idPrestador, idTipoAnimal,
				servicoDetalhadoTipoAnimalEstimacaoConverter.toDomain(request));

		return ResponseEntity.ok(servico);
	}

	@Operation(summary = "Atualiza um tipo de animal aceito para serviço detalhado.")
	@PutMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}/tipoAnimalAceito/tipoAnimal/{idTipoAnimal}")
	public ResponseEntity<ServicoDetalhadoRepresentation> atualizarTipoAnimalAceito(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do serviço detalhado.")
			@PathVariable Long idServico,
			@Parameter(description = "Id do tipo do animal.")
			@PathVariable Integer idTipoAnimal,
			@Parameter(description = "Serviço detalhado a ser atualizado")
			@RequestBody PrecoPorTipoRepresentation request)
			throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/prestador/{}/servico-detalhado/{}/tipoAnimalAceito/tipoAnimal/{}",
				request, idPrestador, idServico, idTipoAnimal);
		var servico = service.atualizarTipoAnimalAceito(idServico, idPrestador, idTipoAnimal,
				servicoDetalhadoTipoAnimalEstimacaoConverter.toDomain(request));
		var representation = converter.toRepresentation(servico);

		return ResponseEntity.ok(representation);
	}

	@Operation(summary = "Busca adicionais atrelados a um serviço")
	@GetMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}/adicional")
	public ResponseEntity<List<AdicionalRepresentation>> buscarAdicionais(
			@Parameter(description = "Id do prestador")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do Serviço")
			@PathVariable Long idServico,
			@Parameter(description = "Flag para verificar se deve-se buscar apenas adicionais ativos ou todos")
			@RequestParam Boolean apenasAtivos) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/prestador/{}/servico-detalhado/{}/adicional?apenasAtivos={}",
				idPrestador, idServico, apenasAtivos);
		var representation = servicoDetalhadoFacade.buscarAdicionais(idPrestador, idServico, apenasAtivos);
		return ResponseEntity.ok(representation);
	}

	@Operation(summary = "Cria novo adicional para um serviço")
	@PostMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}/adicional")
	public ResponseEntity<AdicionalRepresentation> criarAdicional(
			@Parameter(description = "Id do prestador")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do serviço")
			@PathVariable Long idServico,
			@Parameter(description = "Corpo do adicional a adicionar")
			@RequestBody AdicionalRepresentation request)
			throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/prestador/{}/servico-detalhado/{}/adicional",
				request, idPrestador, idServico);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(servicoDetalhadoFacade.criaAdicional(idPrestador, idServico, request));
	}

	@Operation(summary = "Busca serviços detalhados para comparação")
	@GetMapping("/servico-detalhado")
	public ResponseEntity<ComparacaoWrapper> buscarServicosParaComparacao(
			@Parameter(description = "Lista de ID's a buscar")
			@RequestParam(name = "ids") List<Long> idsServicos) throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/servico-detalhado?ids={}", idsServicos);
		var servicos = servicoDetalhadoFacade.buscarServicosDetalhadosPorIds(idsServicos);

		return ResponseEntity.ok(ComparacaoUtils.criaWrapper(servicos));
	}

	@Operation(summary = "Atualiza adicional de um serviço")
	@PutMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}/adicional/{idAdicional}")
	public ResponseEntity<AdicionalRepresentation> atualizarAdicional(
			@Parameter(description = "Id do prestador")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do serviço")
			@PathVariable Long idServico,
			@Parameter(description = "Id do adicional")
			@PathVariable Long idAdicional,
			@Parameter(description = "Corpo do adicional a atualizar")
			@RequestBody AdicionalRepresentation request)
			throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/prestador/{}/servico-detalhado/{}/adicional/{}",
				request, idPrestador, idServico, idAdicional);
		var adicional = servicoDetalhadoFacade.atualizarAdicional(idPrestador, idServico, idAdicional, request);

		return ResponseEntity.ok(adicional);
	}

	@Operation(summary = "Deleta adicional de um serviço")
	@PatchMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}/adicional/{idAdicional}")
	public ResponseEntity<AdicionalRepresentation> desativarAdicional(
			@Parameter(description = "Id do prestador")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do serviço")
			@PathVariable Long idServico,
			@Parameter(description = "Id do adicional")
			@PathVariable Long idAdicional,
			@Parameter(description = "Status novo do adicional")
			@RequestParam Boolean ativo)
			throws Exception {
		log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/prestador/{}/servico-detalhado/{}/adicional/{}?ativo={}",
				idPrestador, idServico, idAdicional, ativo);
		var representation = servicoDetalhadoFacade.desativarAdicional(idPrestador, idServico, idAdicional, ativo);

		return ResponseEntity.ok(representation);
	}
}
