package hyve.petshow.controller;

import static hyve.petshow.util.LogUtils.INFO_REQUEST_CONTROLLER_BODY_MESSAGE;
import static hyve.petshow.util.LogUtils.INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE;
import static hyve.petshow.util.PagingAndSortingUtils.geraPageableOrdemMaisNovo;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hyve.petshow.controller.converter.AgendamentoConverter;
import hyve.petshow.controller.converter.AvaliacaoConverter;
import hyve.petshow.controller.converter.StatusAgendamentoConverter;
import hyve.petshow.controller.representation.AgendamentoRepresentation;
import hyve.petshow.controller.representation.AvaliacaoRepresentation;
import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.controller.representation.NegociacaoRepresentation;
import hyve.petshow.controller.representation.StatusAgendamentoRepresentation;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.facade.AgendamentoFacade;
import hyve.petshow.facade.AvaliacaoFacade;
import hyve.petshow.service.port.AgendamentoService;
import hyve.petshow.service.port.AvaliacaoService;
import hyve.petshow.service.port.StatusAgendamentoService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;

@Slf4j
@RestController
@RequestMapping("/agendamento")
@OpenAPIDefinition(info = @Info(title = "API agendamento", description = "API para CRUD de agendamento"))
public class AgendamentoController {
	@Autowired
	private AgendamentoService agendamentoService;
	@Autowired
	private StatusAgendamentoService statusAgendamentoService;
	@Autowired
	private AvaliacaoService avaliacaoService;
	@Autowired
	private AgendamentoFacade agendamentoFacade;
	@Autowired
	private AvaliacaoFacade avaliacaoFacade;
	@Autowired
	private AgendamentoConverter agendamentoConverter;
	@Autowired
	private StatusAgendamentoConverter statusAgendamentoConverter;
	@Autowired
	private AvaliacaoConverter avaliacaoConverter;

    @Operation(summary = "Adiciona um novo agendamento.")
    @PostMapping
    public ResponseEntity<AgendamentoRepresentation> adicionarAgendamento(
            @Parameter(description = "Agendamento que será inserido")
            @RequestBody AgendamentoRepresentation request) throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/agendamento", request);
        var agendamento = agendamentoFacade.adicionarAgendamento(request);

		return ResponseEntity.ok(agendamento);
	}

    @Operation(summary = "Recupera todos os agendamentos por cliente.")
    @GetMapping("/cliente/{id}")
    public ResponseEntity<Page<AgendamentoRepresentation>> buscarAgendamentosPorCliente(
            @Parameter(description = "Id do cliente cujos agendamentos serão buscados")
            @PathVariable Long id,
            @Parameter(description = "Número da página")
            @RequestParam("pagina") Integer pagina,
            @Parameter(description = "Número de itens")
            @RequestParam("quantidadeItens") Integer quantidadeItens) throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/cliente/{}?pagina={}&quantidadeItens={}", id, pagina, quantidadeItens);
        var agendamentos = agendamentoService.buscarAgendamentosPorCliente(id, geraPageableOrdemMaisNovo(pagina, quantidadeItens));
        var representation = agendamentoConverter.toRepresentationPage(agendamentos);

		return ResponseEntity.ok(representation);
	}

    @Operation(summary = "Recupera todos os agendamentos por prestador.")
    @GetMapping("/prestador/{id}")
    public ResponseEntity<Page<AgendamentoRepresentation>> buscarAgendamentosPorPrestador(
            @Parameter(description = "Id do prestador cujos agendamentos serão buscados")
            @PathVariable Long id,
            @Parameter(description = "Número da página")
            @RequestParam("pagina") Integer pagina,
            @Parameter(description = "Número de itens")
            @RequestParam("quantidadeItens") Integer quantidadeItens) throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/prestador/{}?pagina={}&quantidadeItens={}", id, pagina, quantidadeItens);
        var agendamentos = agendamentoService.buscarAgendamentosPorPrestador(id, geraPageableOrdemMaisNovo(pagina, quantidadeItens));
        var representation = agendamentoConverter.toRepresentationPage(agendamentos);

		return ResponseEntity.ok(representation);
	}

    @Operation(summary = "Recupera um agendamento por ID.")
    @GetMapping("/{id}/usuario/{usuarioId}")
    public ResponseEntity<AgendamentoRepresentation> buscarAgendamentoPorId(
            @Parameter(description = "Id do agendamento")
            @PathVariable Long id,
            @Parameter(description = "Id do usuario que realiza a busca, para verificar se o mesmo pode ter acesso às informações")
            @PathVariable Long usuarioId) throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/{}/usuario/{}", id, usuarioId);
        var agendamento = agendamentoService.buscarPorIdAtivo(id, usuarioId);
        var representation = agendamentoConverter.toRepresentation(agendamento);

		return ResponseEntity.ok(representation);
	}

    @Operation(summary = "Retorna todos os status de agendamento")
    @GetMapping("/statuses")
    public ResponseEntity<List<StatusAgendamentoRepresentation>> buscarStatusAgendamento() throws NotFoundException {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/statuses");
        var statuses = statusAgendamentoService.buscarStatusAgendamento();
        var representation = statusAgendamentoConverter.toRepresentationList(statuses);

        return ResponseEntity.ok(representation);
    }
    /*ATENCAO AO UTILIZAR STATUS EM PRODUCAO DEVIDO AO ID PULAR DE 2 EM 2*/
    @Operation(summary = "Atualiza status do agendamento.")
    @PatchMapping("/{id}/prestador/{prestadorId}/status/{statusId}")
    public ResponseEntity<MensagemRepresentation> atualizarStatusAgendamento(
            @Parameter(description = "Id do agendamento")
            @PathVariable Long id,
            @Parameter(description = "Id do prestador responsável pelo agendamento")
            @PathVariable Long prestadorId,
            @Parameter(description = "Id do novo status do agendamento")
            @PathVariable Integer statusId) throws NotFoundException, BusinessException {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/{}/prestador/{}/status/{}", id, prestadorId, statusId);
        var mensagem = new MensagemRepresentation();
        var response = agendamentoFacade.atualizarStatusAgendamento(id, prestadorId, statusId);

		mensagem.setId(id);
		mensagem.setSucesso(response);

		return ResponseEntity.ok(mensagem);
	}

    @Operation(summary = "Adiciona avaliação.")
    @PostMapping("/{id}/avaliacao")
    public ResponseEntity<AvaliacaoRepresentation> adicionarAvaliacao(
            @Parameter(description = "Id do agendamento.")
            @PathVariable Long id,
            @Parameter(description = "Avaliação que será inserida")
            @RequestBody AvaliacaoRepresentation request)
            throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/agendamento/{}/avaliacao", request, id);
        var avaliacao = avaliacaoFacade.adicionarAvaliacao(request, id);
        var representation = avaliacaoConverter.toRepresentation(avaliacao);

		return ResponseEntity.status(HttpStatus.CREATED).body(representation);
	}

    @Operation(summary = "Busca avaliação por agendamento.")
    @GetMapping("/{id}/avaliacao")
    public ResponseEntity<AvaliacaoRepresentation> buscarAvaliacaoPorAgendamento(
            @Parameter(description = "Id do agendamento.")
            @PathVariable Long id)
            throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/{}/avaliacao", id);
        var avaliacao = avaliacaoService.buscarAvaliacaoPorAgendamentoId(id);
        var representation = avaliacaoConverter.toRepresentation(avaliacao);

        return ResponseEntity.ok(representation);
    }
    
    @Operation(summary = "Busca horários já agendados.")
    @GetMapping("/prestador/{idPrestador}/horarios")
    public ResponseEntity<List<String>> buscarHorariosAgendamento(
    		@Parameter(description = "Id do prestador")
    		@PathVariable Long idPrestador,
    		@Parameter(description = "Data do agendamento")
    		@RequestParam("dataAgendamento") 
    		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAgendamento) {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/prestador/{}/horarios?dataAgendamento={}", idPrestador, dataAgendamento);
        var representation = agendamentoFacade.buscaHorariosAgendamento(idPrestador, dataAgendamento);

    	return ResponseEntity.ok(representation);
    }

    @Operation(summary = "Deleta agendamento")
    @DeleteMapping("/{agendamentoId}/cliente/{clienteId}")
    public ResponseEntity<?> deletarAgendamento(
            @Parameter(description = "Id do agendamento")
            @PathVariable Long agendamentoId,
            @Parameter(description = "Id do cliente")
            @PathVariable Long clienteId) throws NotFoundException, BusinessException {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/{}/cliente/{}", agendamentoId, clienteId);
        agendamentoService.deletarAgendamento(agendamentoId, clienteId);

		return ResponseEntity.noContent().build();
	}

    @Operation(summary = "Ativa agendamento")
    @PatchMapping("/{agendamentoId}/cliente/{clienteId}")
    public ResponseEntity<AgendamentoRepresentation> ativaAgendamento(
            @Parameter(description = "Id do agendamento")
            @PathVariable Long agendamentoId,
            @Parameter(description = "Id do cliente")
            @PathVariable Long clienteId) throws NotFoundException, BusinessException {
        log.info(INFO_REQUEST_CONTROLLER_RETRIEVE_MESSAGE, "/agendamento/{}/cliente/{}", agendamentoId, clienteId);
        var agendamento = agendamentoService.ativarAgendamento(agendamentoId, clienteId);
        var representation = agendamentoConverter.toRepresentation(agendamento);

		return ResponseEntity.ok(representation);
	}

	@Operation(summary = "Confirma negociação")
	@PatchMapping("/{agendamentoId}/prestador/{prestadorId}/negociacao")
	public ResponseEntity<AgendamentoRepresentation> confirmaNegociacao(
			@Parameter(description = "Id do agendamento") @PathVariable Long agendamentoId,
			@Parameter(description = "Id do prestador") @PathVariable Long prestadorId,
			@RequestBody NegociacaoRepresentation negociacaoRepresentation)
			throws NotFoundException, BusinessException {
		var agendamento = agendamentoFacade.confirmaNegociacao(agendamentoId, prestadorId, negociacaoRepresentation);
		return ResponseEntity.ok(agendamento);
	}