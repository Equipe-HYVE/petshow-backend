package hyve.petshow.unit.facade;

import hyve.petshow.controller.converter.AdicionalConverter;
import hyve.petshow.controller.converter.PrestadorConverter;
import hyve.petshow.controller.converter.ServicoDetalhadoConverter;
import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.controller.representation.AdicionalRepresentation;
import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.controller.representation.PrestadorRepresentation;
import hyve.petshow.controller.representation.ServicoDetalhadoRepresentation;
import hyve.petshow.domain.Adicional;
import hyve.petshow.domain.Prestador;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.facade.ServicoDetalhadoFacade;
import hyve.petshow.service.port.AdicionalService;
import hyve.petshow.service.port.PrestadorService;
import hyve.petshow.service.port.ServicoDetalhadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static hyve.petshow.mock.AdicionalMock.criaAdicional;
import static hyve.petshow.mock.AdicionalMock.criaAdicionalRepresentation;
import static hyve.petshow.mock.MensagemMock.criaMensagemRepresentationSucesso;
import static hyve.petshow.mock.PrestadorMock.criaPrestador;
import static hyve.petshow.mock.PrestadorMock.criaPrestadorRepresentation;
import static hyve.petshow.mock.ServicoDetalhadoMock.*;
import static hyve.petshow.util.PagingAndSortingUtils.geraPageable;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ServicoDetalhadoFacadeTest {
    @Mock
    private ServicoDetalhadoService servicoDetalhadoService;
    @Mock
    private PrestadorService prestadorService;
    @Mock
    private ServicoDetalhadoConverter servicoDetalhadoConverter;
    @Mock
    private PrestadorConverter prestadorConverter;
    @Mock
    private AdicionalService adicionalService;
    @Mock
    private AdicionalConverter adicionalConverter;
    @InjectMocks
    private ServicoDetalhadoFacade servicoDetalhadoFacade;

    private Page<ServicoDetalhadoRepresentation> servicoDetalhadoRepresentationPage =
            new PageImpl<>(criaServicoDetalhadoRepresentationList());
    private Page<ServicoDetalhado> servicoDetalhadoPage =
            new PageImpl<>(criaServicoDetalhadoList());
    private PrestadorRepresentation prestadorRepresentation = criaPrestadorRepresentation();
    private Prestador prestador = criaPrestador();
    private ServicoDetalhadoRepresentation servicoDetalhadoRepresentation =
            criaServicoDetalhadoRepresentation();
    private ServicoDetalhado servicoDetalhado = criaServicoDetalhado();
    private List<AdicionalRepresentation> adicionaisRepresentation = Arrays.asList(criaAdicionalRepresentation(1L));
    private List<Adicional> adicionais = Arrays.asList(criaAdicional(1L));
    private AdicionalRepresentation adicionalRepresentation = criaAdicionalRepresentation(1L);
    private Adicional adicional = criaAdicional(1L);
    private List<ServicoDetalhado> servicosDetalhados = Arrays.asList(servicoDetalhado);
    private List<ServicoDetalhadoRepresentation> servicosDetalhadosRepresentation =
            Arrays.asList(servicoDetalhadoRepresentation);
    private MensagemRepresentation mensagemRepresentation = criaMensagemRepresentationSucesso();
    private Pageable pageable = geraPageable(0, 10);
    private static ServicoDetalhadoFilter servicoDetalhadoFilter = new ServicoDetalhadoFilter();

    static {
        servicoDetalhadoFilter.setTipoServicoId(1);
    }

    @BeforeEach
    public void init() throws Exception {
        openMocks(this);

        doReturn(servicoDetalhadoPage).when(servicoDetalhadoService)
                .buscarServicosDetalhadosPorTipoServico(any(Pageable.class), any(ServicoDetalhadoFilter.class));
        doReturn(servicoDetalhadoRepresentationPage).when(servicoDetalhadoConverter).toRepresentationPage(any(Page.class));
        doReturn(prestador).when(prestadorService).buscarPorId(anyLong());
        doReturn(prestadorRepresentation).when(prestadorConverter).toRepresentation(any(Prestador.class));
        doReturn(servicoDetalhado).when(servicoDetalhadoService).buscarPorPrestadorIdEServicoId(anyLong(), anyLong());
        doReturn(servicoDetalhadoRepresentation).when(servicoDetalhadoConverter).toRepresentation(any(ServicoDetalhado.class));
        doReturn(adicionais).when(adicionalService).buscarAtivosPorServicoDetalhado(anyLong());
        doReturn(adicionaisRepresentation).when(adicionalConverter).toRepresentationList(anyList());
        doReturn(adicional).when(adicionalConverter).toDomain(any(AdicionalRepresentation.class));
        doReturn(adicional).when(adicionalService).criarAdicional(any(Adicional.class), anyLong());
        doReturn(adicionalRepresentation).when(adicionalConverter).toRepresentation(any(Adicional.class));
        doReturn(servicosDetalhados).when(servicoDetalhadoService).buscarServicosDetalhadosPorIds(anyList());
        doReturn(servicosDetalhadosRepresentation).when(servicoDetalhadoConverter).toRepresentationList(anyList());
        doReturn(adicional).when(adicionalService).atualizarAdicional(anyLong(), any(Adicional.class));
        doReturn(TRUE).when(adicionalService).desativarAdicional(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    public void deve_buscar_servicos_detalhados_por_tipo_servico() throws Exception {
        var actual = servicoDetalhadoFacade.buscarServicosDetalhadosPorTipoServico(pageable, servicoDetalhadoFilter);

        assertEquals(servicoDetalhadoRepresentationPage, actual);
    }

    @Test
    public void deve_buscar_por_prestador_id_e_servico_id() throws Exception {
        var actual = servicoDetalhadoFacade.buscarPorPrestadorIdEServicoId(1L, 1L);

        assertEquals(servicoDetalhadoRepresentation, actual);
    }

    @Test
    public void deve_buscar_adicionais() throws Exception {
        var actual = servicoDetalhadoFacade.buscarAdicionais(1L, 1L, TRUE);

        assertEquals(adicionaisRepresentation, actual);
    }

    @Test
    public void deve_criar_adicional() throws Exception {
        var actual = servicoDetalhadoFacade.criaAdicional(1L, 1L, adicionalRepresentation);

        assertEquals(adicionalRepresentation, actual);
    }

    @Test
    public void deve_buscar_servicos_detalhados_por_ids() throws Exception {
        var actual = servicoDetalhadoFacade.buscarServicosDetalhadosPorIds(Arrays.asList(1L));

        assertEquals(servicosDetalhadosRepresentation, actual);
    }

    @Test
    public void deve_atualizar_adicional() throws Exception {
        var actual = servicoDetalhadoFacade.atualizarAdicional(1L, 1L, 1L, adicionalRepresentation);

        assertEquals(adicionalRepresentation, actual);
    }

    @Test
    public void deve_desativar_adicional() throws Exception {
        var actual = servicoDetalhadoFacade.desativarAdicional(1L, 1L, 1L, TRUE);

        assertEquals(mensagemRepresentation, actual);
    }
}
