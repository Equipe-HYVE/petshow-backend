package hyve.petshow.controller;

import hyve.petshow.controller.converter.ServicoDetalhadoConverter;
import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.controller.representation.*;
import hyve.petshow.facade.AvaliacaoFacade;
import hyve.petshow.facade.ServicoDetalhadoFacade;
import hyve.petshow.service.port.ServicoDetalhadoService;
import hyve.petshow.util.ComparacaoUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hyve.petshow.util.PagingAndSortingUtils.geraPageable;

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
		var servicosDetalhados = servicoDetalhadoFacade
				.buscarServicosDetalhadosPorTipoServico(geraPageable(pagina, quantidadeItens), filtragem);
		var response = ResponseEntity.ok(servicosDetalhados);

		return response;
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
		var avaliacoes = avaliacaoFacade.buscarAvaliacaoPorServico(id, geraPageable(pagina, quantidadeItens));

		return ResponseEntity.ok(avaliacoes);
	}

	@Operation(summary = "Adiciona serviço detalhado para prestador.")
	@PostMapping("/prestador/{idPrestador}/servico-detalhado")
	public ResponseEntity<ServicoDetalhadoRepresentation> adicionarServicoDetalhado(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long idPrestador,
			@Parameter(description = "Serviço que será inserido.")
			@RequestBody ServicoDetalhadoRepresentation request) {
		var servico = converter.toDomain(request);
		servico.setPrestadorId(idPrestador);
		servico = service.adicionarServicoDetalhado(servico);
		var representation = converter.toRepresentation(servico);

		return ResponseEntity.status(HttpStatus.CREATED).body(representation);
	}

	@Operation(summary = "Deleta serviço detalhado por prestador e pelo próprio id.")
	@DeleteMapping("/prestador/{prestadorId}/servico-detalhado/{id}")
	public ResponseEntity<MensagemRepresentation> removerServicoDetalhado(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long prestadorId,
			@Parameter(description = "Id do serviço detalhado.")
			@PathVariable Long id) throws Exception {
		var response = service.removerServicoDetalhado(id, prestadorId);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Busca serviço detalhado.")
	@GetMapping("/prestador/{prestadorId}/servico-detalhado/{servicoId}")
	public ResponseEntity<ServicoDetalhadoRepresentation> buscarPorPrestadorIdEServicoId(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long prestadorId,
			@Parameter(description = "Id do serviço detalhado.")
			@PathVariable Long servicoId) throws Exception {
		var servicoDetalhado = servicoDetalhadoFacade.buscarPorPrestadorIdEServicoId(prestadorId, servicoId);

		return ResponseEntity.ok(servicoDetalhado);
	}

	@Operation(summary = "Atualiza serviço detalhado.")
	@PutMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}")
	public ResponseEntity<ServicoDetalhadoRepresentation> atualizarServicoDetalhado(
			@Parameter(description = "Id do prestador.")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do serviço detalhado.")
			@PathVariable Long idServico,
			@Parameter(description = "Serviço detalhado a ser atualizado")
			@RequestBody ServicoDetalhadoRepresentation request)
			throws Exception {
		var servico = converter.toDomain(request);
		servico = service.atualizarServicoDetalhado(idServico, idPrestador, servico);
		var representation = converter.toRepresentation(servico);

		return ResponseEntity.ok(representation);
	}

	@Operation(summary = "Busca adicionais atrelados a um serviço")
	@GetMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}/adicional")
	public ResponseEntity<List<AdicionalRepresentation>> buscarAdicionais(
			@Parameter(description = "Id do prestador")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do Serviço")
			@PathVariable Long idServico) throws Exception {
		return ResponseEntity.ok(servicoDetalhadoFacade.buscarAdicionais(idPrestador, idServico));
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
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(servicoDetalhadoFacade.criaAdicional(idPrestador, idServico, request));
	}

	@Operation(summary = "Busca serviços detalhados para comparação")
	@GetMapping("/servico-detalhado")
	public ResponseEntity<ComparacaoWrapper> buscarServicosParaComparacao(
			@Parameter(description = "Lista de ID's a buscar")
			@RequestParam(name = "ids") List<Long> idsServicos) throws Exception {
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
		var adicional = servicoDetalhadoFacade.atualizarAdicional(idPrestador, idServico, idAdicional, request);

		return ResponseEntity.ok(adicional);
	}

	@Operation(summary = "Deleta adicional de um serviço")
	@DeleteMapping("/prestador/{idPrestador}/servico-detalhado/{idServico}/adicional/{idAdicional}")
	public ResponseEntity<MensagemRepresentation> desativarAdicional(
			@Parameter(description = "Id do prestador")
			@PathVariable Long idPrestador,
			@Parameter(description = "Id do serviço")
			@PathVariable Long idServico,
			@Parameter(description = "Id do adicional")
			@PathVariable Long idAdicional)
			throws Exception {
		var mensagem = servicoDetalhadoFacade.desativarAdicional(idPrestador, idServico, idAdicional);

		return ResponseEntity.ok(mensagem);
	}
}
