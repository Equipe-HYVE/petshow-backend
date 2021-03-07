package hyve.petshow.unit.controller;

import hyve.petshow.controller.ServicoDetalhadoController;
import hyve.petshow.controller.converter.ServicoDetalhadoConverter;
import hyve.petshow.controller.converter.ServicoDetalhadoTipoAnimalEstimacaoConverter;
import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.controller.representation.*;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.domain.ServicoDetalhadoTipoAnimalEstimacao;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.facade.AvaliacaoFacade;
import hyve.petshow.facade.ServicoDetalhadoFacade;
import hyve.petshow.service.port.ServicoDetalhadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static hyve.petshow.mock.AdicionalMock.criaAdicionalRepresentation;
import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacaoRepresentation;
import static hyve.petshow.mock.MensagemMock.criaMensagemRepresentationSucesso;
import static hyve.petshow.mock.ServicoDetalhadoMock.*;
import static hyve.petshow.util.AuditoriaUtils.INATIVO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.openMocks;

public class ServicoDetalhadoControllerTest {
	@Mock
	private ServicoDetalhadoService service;
	@Mock
	private ServicoDetalhadoConverter converter;
	@Mock
	private AvaliacaoFacade avaliacaoFacade;
	@Mock
	private ServicoDetalhadoFacade servicoDetalhadoFacade;
	@Mock
	private ServicoDetalhadoTipoAnimalEstimacaoConverter servicoDetalhadoTipoAnimalEstimacaoConverter;
	@InjectMocks
	private ServicoDetalhadoController controller;

	private ServicoDetalhado servicoDetalhado = criaServicoDetalhado();
	private ServicoDetalhadoRepresentation servicoDetalhadoRepresentation = criaServicoDetalhadoRepresentation();

	private PrecoPorTipoRepresentation precoPorTipoRepresentation = criaPrecoPorTipoRepresentation();
	private ServicoDetalhadoTipoAnimalEstimacao servicoDetalhadoTipoAnimalEstimacao = criaServicoDetalhadoTipoAnimalEstimacao();

	private List<ServicoDetalhado> servicoDetalhadoList = criaServicoDetalhadoList();
	private List<ServicoDetalhadoRepresentation> servicoDetalhadoRepresentationList = criaServicoDetalhadoRepresentationList();
	private Page<ServicoDetalhado> servicoDetalhadoPage = new PageImpl<>(servicoDetalhadoList);
	private Page<ServicoDetalhadoRepresentation> servicoDetalhadoRepresentationPage = new PageImpl<>(
			servicoDetalhadoRepresentationList);

	private MensagemRepresentation mensagemRepresentation = criaMensagemRepresentationSucesso();

	private Page<AvaliacaoRepresentation> avaliacaoRepresentationPage =
			new PageImpl<>(Arrays.asList(criaAvaliacaoRepresentation()));

	private ComparacaoWrapper comparacaoWrapper = new ComparacaoWrapper();
	private AdicionalRepresentation adicionalRepresentation = criaAdicionalRepresentation(1L);

	@BeforeEach
	public void init() throws Exception {
		openMocks(this);

		doReturn(servicoDetalhadoPage).when(service).buscarPorPrestadorId(anyLong(), any(Pageable.class));
		doReturn(servicoDetalhado).when(service).adicionarServicoDetalhado(any(ServicoDetalhado.class));
		doReturn(servicoDetalhado).when(service).atualizarServicoDetalhado(anyLong(), anyLong(), anyBoolean());
		doReturn(servicoDetalhado).when(service).buscarPorPrestadorIdEServicoId(anyLong(), anyLong());
		doReturn(servicoDetalhado).when(service).adicionarTipoAnimalAceito(anyLong(), anyLong(),
				any(ServicoDetalhadoTipoAnimalEstimacao.class));
		doReturn(servicoDetalhadoPage).when(service).buscarServicosDetalhadosPorTipoServico(any(Pageable.class),
				any(ServicoDetalhadoFilter.class));
		doReturn(servicoDetalhado).when(converter).toDomain(any(ServicoDetalhadoRepresentation.class));
		doReturn(servicoDetalhadoRepresentation).when(converter).toRepresentation(any(ServicoDetalhado.class));
		doReturn(servicoDetalhadoRepresentationList).when(converter).toRepresentationList(anyList());
		doReturn(servicoDetalhadoRepresentationPage).when(converter).toRepresentationPage(any(Page.class));
		doReturn(servicoDetalhadoRepresentation).when(servicoDetalhadoFacade).buscarPorPrestadorIdEServicoId(anyLong(),
				anyLong());
		doReturn(servicoDetalhadoRepresentationPage).when(servicoDetalhadoFacade)
				.buscarServicosDetalhadosPorTipoServico(any(Pageable.class), any(ServicoDetalhadoFilter.class));
		doReturn(avaliacaoRepresentationPage).when(avaliacaoFacade)
				.buscarAvaliacaoPorServico(anyLong(), any(Pageable.class));
		doReturn(servicoDetalhadoRepresentationList).when(servicoDetalhadoFacade)
				.buscarServicosDetalhadosPorIds(anyList());
		doReturn(adicionalRepresentation).when(servicoDetalhadoFacade)
				.atualizarAdicional(anyLong(), anyLong(), anyLong(), any(AdicionalRepresentation.class));
		doReturn(mensagemRepresentation).when(servicoDetalhadoFacade).desativarAdicional(anyLong(), anyLong(), anyLong(), anyBoolean());
		doReturn(servicoDetalhadoTipoAnimalEstimacao)
				.when(servicoDetalhadoTipoAnimalEstimacaoConverter).toDomain(any(PrecoPorTipoRepresentation.class));
		doReturn(precoPorTipoRepresentation).when(servicoDetalhadoTipoAnimalEstimacaoConverter)
				.toRepresentation(any(ServicoDetalhadoTipoAnimalEstimacao.class));
		doReturn(servicoDetalhadoRepresentation).when(servicoDetalhadoFacade).adicionarTipoAnimalAceito(anyLong(),
				anyLong(), anyInt(), any(ServicoDetalhadoTipoAnimalEstimacao.class));
	}

	@Test
	public void deve_retornar_todos_os_servicos_detalhados_de_um_prestador() throws Exception {
		var expected = ResponseEntity.ok(servicoDetalhadoRepresentationPage);

		var actual = controller.buscarServicosDetalhadosPorPrestador(1L, 0, 5);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_adicionar_e_retornar_servico_detalhado() throws BusinessException {
		var expected = ResponseEntity.status(HttpStatus.CREATED).body(servicoDetalhadoRepresentation);

		var actual = controller.adicionarServicoDetalhado(1L, servicoDetalhadoRepresentation);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_atualizar_servico_detalhado() throws Exception {
		var expected = ResponseEntity.ok(servicoDetalhadoRepresentation);

		var actual = controller.atualizarServicoDetalhado(1L, 1L, Boolean.FALSE);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_retornar_servico_detalhado() throws Exception {
		var expected = ResponseEntity.ok(servicoDetalhadoRepresentation);

		var actual = controller.buscarPorPrestadorIdEServicoId(1L, 1L);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_atualizar_e_retornar_servico_detalhado() throws Exception {
		var expected = ResponseEntity.ok(servicoDetalhadoRepresentation);

		var actual = controller.adicionarTipoAnimalAceito(1L, 1L, 1, precoPorTipoRepresentation);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_retornar_todos_os_servicos_detalhados_de_um_tipo() throws Exception {
		var expected = ResponseEntity.ok(servicoDetalhadoRepresentationPage);

		var actual = controller.buscarServicosDetalhadosPorTipoServico(0, 5,
				new ServicoDetalhadoFilter());

		assertEquals(expected, actual);
	}

	@Test
	public void deve_retornar_todos_os_adicionais_de_um_servico() throws Exception {
		var adicionais = new ArrayList<AdicionalRepresentation>() {
			private static final long serialVersionUID = 1L;

			{
				add(AdicionalRepresentation.builder().id(1l).nome("Teste").servicoDetalhadoId(1l)
						.preco(BigDecimal.valueOf(10)).build());
				add(AdicionalRepresentation.builder().id(2l).nome("Teste").servicoDetalhadoId(1l)
						.preco(BigDecimal.valueOf(10)).build());
			}
		};
		var expected = ResponseEntity.ok(adicionais);

		doReturn(adicionais).when(servicoDetalhadoFacade).buscarAdicionais(anyLong(), anyLong(), anyBoolean());

		var busca = controller.buscarAdicionais(1l, 1l, Boolean.TRUE);

		assertEquals(expected, busca);
	}

	@Test
	public void deve_retornar_excecao_ao_nao_encontrar_servicos() throws Exception {
		doThrow(NotFoundException.class).when(servicoDetalhadoFacade).buscarAdicionais(anyLong(), anyLong(), anyBoolean());
		assertThrows(NotFoundException.class, () -> controller.buscarAdicionais(1l, 1l, Boolean.TRUE));
	}

	@Test
	public void deve_criar_novo_adicional() throws Exception {
		var adicionalTeste = AdicionalRepresentation.builder().id(1l).nome("Teste").servicoDetalhadoId(1l)
				.preco(BigDecimal.valueOf(23)).build();

		var dbMock = new ArrayList<AdicionalRepresentation>();
		Mockito.doAnswer(mock -> {
			var adicional = (AdicionalRepresentation) mock.getArgument(2);
			dbMock.add(adicional);
			return adicional;
		}).when(servicoDetalhadoFacade).criaAdicional(anyLong(), anyLong(), any());

		controller.criarAdicional(1l, 1l, adicionalTeste);

		assertEquals(1, dbMock.size());
	}

	@Test
	public void deve_buscar_avaliacoes_por_servico_detalhado() throws Exception {
		var actual = controller.buscarAvaliacoesPorServicoDetalhado(1L, 0,10);
		var expected = ResponseEntity.ok(avaliacaoRepresentationPage);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_buscar_servicos_para_comparacao() throws Exception {
		var actual = controller.buscarServicosParaComparacao(Arrays.asList(1L, 2L));
		var expected = ResponseEntity.ok(comparacaoWrapper);

		assertEquals(expected.getStatusCode(), actual.getStatusCode());
	}

	@Test
	public void deve_atualizar_adicional() throws Exception {
		var actual = controller.atualizarAdicional(1L, 1L, 1L, adicionalRepresentation);
		var expected = ResponseEntity.ok(adicionalRepresentation);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_desativar_adicional() throws Exception {
		var actual = controller.desativarAdicional(1L, 1L, 1L, Boolean.FALSE);
		var expected = ResponseEntity.ok(mensagemRepresentation);

		assertEquals(expected, actual);
	}
}
