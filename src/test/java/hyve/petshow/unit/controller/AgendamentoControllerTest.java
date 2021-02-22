package hyve.petshow.unit.controller;

import hyve.petshow.controller.AgendamentoController;
import hyve.petshow.controller.converter.AgendamentoConverter;
import hyve.petshow.controller.converter.AvaliacaoConverter;
import hyve.petshow.controller.converter.StatusAgendamentoConverter;
import hyve.petshow.controller.representation.AgendamentoRepresentation;
import hyve.petshow.controller.representation.AvaliacaoRepresentation;
import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.controller.representation.StatusAgendamentoRepresentation;
import hyve.petshow.domain.Agendamento;
import hyve.petshow.domain.Avaliacao;
import hyve.petshow.domain.StatusAgendamento;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.facade.AgendamentoFacade;
import hyve.petshow.facade.AvaliacaoFacade;
import hyve.petshow.service.port.AgendamentoService;
import hyve.petshow.service.port.AvaliacaoService;
import hyve.petshow.service.port.StatusAgendamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static hyve.petshow.mock.AgendamentoMock.criaAgendamento;
import static hyve.petshow.mock.AgendamentoMock.criaAgendamentoRepresentation;
import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacao;
import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacaoRepresentation;
import static hyve.petshow.mock.MensagemMock.criaMensagemRepresentationSucesso;
import static hyve.petshow.mock.StatusAgendamentoMock.criaStatusAgendamento;
import static hyve.petshow.mock.StatusAgendamentoMock.criaStatusAgendamentoRepresentation;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AgendamentoControllerTest {
    @Mock
    private AgendamentoService agendamentoService;
    @Mock
    private StatusAgendamentoService statusAgendamentoService;
    @Mock
    private AvaliacaoService avaliacaoService;
    @Mock
    private AgendamentoFacade agendamentoFacade;
    @Mock
    private AvaliacaoFacade avaliacaoFacade;
    @Mock
    private AgendamentoConverter agendamentoConverter;
    @Mock
    private StatusAgendamentoConverter statusAgendamentoConverter;
    @Mock
    private AvaliacaoConverter avaliacaoConverter;
    @InjectMocks
    private AgendamentoController agendamentoController;

    private Agendamento agendamento = criaAgendamento();
    private AgendamentoRepresentation agendamentoRepresentation = criaAgendamentoRepresentation();
    private List<Agendamento> agendamentoList = Arrays.asList(agendamento);
    private List<AgendamentoRepresentation> agendamentoRepresentationList = Arrays.asList(agendamentoRepresentation);
    private Page<Agendamento> agendamentoPage = new PageImpl<>(agendamentoList);
    private Page<AgendamentoRepresentation> agendamentoRepresentationPage = new PageImpl<>(agendamentoRepresentationList);
    private List<StatusAgendamento> statusAgendamentoList = Arrays.asList(criaStatusAgendamento());
    private List<StatusAgendamentoRepresentation> statusAgendamentoRepresentationList =
            Arrays.asList(criaStatusAgendamentoRepresentation());
    private MensagemRepresentation mensagemRepresentation = criaMensagemRepresentationSucesso();
    private Avaliacao avaliacao = criaAvaliacao();
    private AvaliacaoRepresentation avaliacaoRepresentation = criaAvaliacaoRepresentation();


    @BeforeEach
    public void init() throws Exception {
        openMocks(this);

        doReturn(agendamentoRepresentation).when(agendamentoFacade)
                .adicionarAgendamento(any(AgendamentoRepresentation.class));
        doReturn(agendamentoPage).when(agendamentoService).buscarAgendamentosPorCliente(anyLong(), any(Pageable.class));
        doReturn(agendamentoPage).when(agendamentoService).buscarAgendamentosPorPrestador(anyLong(), any(Pageable.class));
        doReturn(agendamentoRepresentationPage).when(agendamentoConverter).toRepresentationPage(any(Page.class));
        doReturn(agendamento).when(agendamentoService).buscarPorId(anyLong(), anyLong());
        doReturn(agendamentoRepresentation).when(agendamentoConverter).toRepresentation(any(Agendamento.class));
        doReturn(statusAgendamentoList).when(statusAgendamentoService).buscarStatusAgendamento();
        doReturn(statusAgendamentoRepresentationList).when(statusAgendamentoConverter).toRepresentationList(anyList());
        doReturn(TRUE).when(agendamentoFacade).atualizarStatusAgendamento(anyLong(), anyLong(), anyInt());
        doReturn(avaliacao).when(avaliacaoFacade).adicionarAvaliacao(any(AvaliacaoRepresentation.class), anyLong());
        doReturn(avaliacao).when(avaliacaoService).buscarAvaliacaoPorAgendamentoId(anyLong());
        doReturn(avaliacaoRepresentation).when(avaliacaoConverter).toRepresentation(any(Avaliacao.class));
    }

    @Test
    public void deve_adicionar_agendamento() throws Exception {
        var actual = agendamentoController.adicionarAgendamento(agendamentoRepresentation);
        var expected = ResponseEntity.ok(agendamentoRepresentation);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_buscar_agendamentos_por_cliente() throws Exception {
        var actual = agendamentoController.buscarAgendamentosPorCliente(1L, 0, 10);
        var expected = ResponseEntity.ok(agendamentoRepresentationPage);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_buscar_agendamentos_por_prestador() throws Exception {
        var actual = agendamentoController.buscarAgendamentosPorPrestador(1L, 0, 10);
        var expected = ResponseEntity.ok(agendamentoRepresentationPage);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_buscar_agendamento_por_id() throws Exception {
        var actual = agendamentoController.buscarAgendamentoPorId(1L, 1L);
        var expected = ResponseEntity.ok(agendamentoRepresentation);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_buscar_status_agendamento() throws NotFoundException {
        var actual = agendamentoController.buscarStatusAgendamento();
        var expected = ResponseEntity.ok(statusAgendamentoRepresentationList);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_atualizar_status_agendamento() throws Exception {
        var actual = agendamentoController.atualizarStatusAgendamento(1L, 1L, 1);
        var expected = ResponseEntity.ok(mensagemRepresentation);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_adicionar_avaliacao() throws Exception {
        var actual = agendamentoController.adicionarAvaliacao(1L, avaliacaoRepresentation);
        var expected = ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoRepresentation);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_buscar_avaliacao_por_agendamento() throws Exception {
        var actual = agendamentoController.buscarAvaliacaoPorAgendamento(1L);
        var expected = ResponseEntity.ok(avaliacaoRepresentation);

        assertEquals(expected, actual);
    }
}
