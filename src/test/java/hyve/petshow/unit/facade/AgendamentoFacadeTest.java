package hyve.petshow.unit.facade;

import hyve.petshow.controller.converter.AgendamentoConverter;
import hyve.petshow.controller.representation.AgendamentoRepresentation;
import hyve.petshow.domain.*;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.facade.AgendamentoFacade;
import hyve.petshow.service.port.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static hyve.petshow.mock.AdicionalMock.criaAdicional;
import static hyve.petshow.mock.AgendamentoMock.criaAgendamento;
import static hyve.petshow.mock.AgendamentoMock.criaAgendamentoRepresentation;
import static hyve.petshow.mock.AnimalEstimacaoMock.criaAnimalEstimacao;
import static hyve.petshow.mock.ClienteMock.criaCliente;
import static hyve.petshow.mock.PrestadorMock.criaPrestador;
import static hyve.petshow.mock.ServicoDetalhadoMock.criaServicoDetalhado;
import static hyve.petshow.mock.StatusAgendamentoMock.criaStatusAgendamento;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AgendamentoFacadeTest {
    @Mock
    private ClienteService clienteService;
    @Mock
    private PrestadorService prestadorService;
    @Mock
    private AgendamentoService agendamentoService;
    @Mock
    private ServicoDetalhadoService servicoDetalhadoService;
    @Mock
    private AnimalEstimacaoService animalEstimacaoService;
    @Mock
    private AdicionalService adicionalService;
    @Mock
    private StatusAgendamentoService statusAgendamentoService;
    @Mock
    private AgendamentoConverter agendamentoConverter;
    @InjectMocks
    private AgendamentoFacade agendamentoFacade;

    private Agendamento agendamento = criaAgendamento();
    private Cliente cliente = criaCliente();
    private Prestador prestador = criaPrestador();
    private ServicoDetalhado servicoDetalhado = criaServicoDetalhado();
    private List<AnimalEstimacao> animaisEstimacao = Arrays.asList(criaAnimalEstimacao());
    private List<Adicional> adicionais = Arrays.asList(criaAdicional(1L));
    private AgendamentoRepresentation agendamentoRepresentation = criaAgendamentoRepresentation();
    private StatusAgendamento statusAgendamento = criaStatusAgendamento();

    @BeforeEach
    public void init() throws Exception {
        openMocks(this);

        doReturn(agendamento).when(agendamentoConverter).toDomain(any(AgendamentoRepresentation.class));
        doReturn(cliente).when(clienteService).buscarPorId(anyLong());
        doReturn(prestador).when(prestadorService).buscarPorId(anyLong());
        doReturn(servicoDetalhado).when(servicoDetalhadoService).buscarPorId(anyLong());
        doReturn(animaisEstimacao).when(animalEstimacaoService).buscarAnimaisEstimacaoPorIds(anyLong(), anyList());
        doReturn(adicionais).when(adicionalService).buscarAdicionaisPorIds(anyLong(), anyList());
        doReturn(agendamento).when(agendamentoService).adicionarAgendamento(any(Agendamento.class));
        doReturn(agendamentoRepresentation).when(agendamentoConverter).toRepresentation(any(Agendamento.class));
        doReturn(statusAgendamento).when(statusAgendamentoService).buscarStatusAgendamento(anyInt());
        doReturn(agendamento).when(agendamentoService).atualizarStatusAgendamento(anyLong(), anyLong(), any(StatusAgendamento.class));
    }

    @Test
    public void deve_adicionar_agendamento() throws Exception {
        var actual = agendamentoFacade.adicionarAgendamento(agendamentoRepresentation);

        assertEquals(agendamentoRepresentation, actual);
    }

    @Test
    public void deve_atualizar_status_agendamento() throws NotFoundException, BusinessException {
        var actual = agendamentoFacade.atualizarStatusAgendamento(1L, 1L, 1);

        assertTrue(actual);
    }
}
