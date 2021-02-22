package hyve.petshow.unit.facade;

import hyve.petshow.controller.converter.AvaliacaoConverter;
import hyve.petshow.controller.representation.AvaliacaoRepresentation;
import hyve.petshow.domain.Avaliacao;
import hyve.petshow.domain.Cliente;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.facade.AvaliacaoFacade;
import hyve.petshow.service.port.AvaliacaoService;
import hyve.petshow.service.port.ClienteService;
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

import java.util.ArrayList;
import java.util.List;

import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacao;
import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacaoRepresentation;
import static hyve.petshow.mock.ClienteMock.criaCliente;
import static hyve.petshow.mock.ServicoDetalhadoMock.criaServicoDetalhado;
import static hyve.petshow.util.PagingAndSortingUtils.geraPageable;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AvaliacaoFacadeTest {
	@Mock
	private ServicoDetalhadoService servicoDetalhadoService;
	@Mock
	private AvaliacaoService avaliacaoService;
	@Mock
	private ClienteService clienteService;
	@Mock
	private AvaliacaoConverter converter;
	@InjectMocks
	private AvaliacaoFacade facade;

	private Cliente cliente = criaCliente();
	private ServicoDetalhado servicoDetalhado = criaServicoDetalhado();
	private AvaliacaoRepresentation avaliacaoRepresentation = criaAvaliacaoRepresentation();
	private List<Avaliacao> avaliacoes = new ArrayList<>();
	private Avaliacao avaliacao = criaAvaliacao();
	private Page<Avaliacao> avaliacaoPage = new PageImpl<>(avaliacoes);
	private Page<AvaliacaoRepresentation> avaliacaoRepresentationPage = new PageImpl<>(singletonList(avaliacaoRepresentation));

	@BeforeEach
	public void init() throws Exception {
		openMocks(this);

		doReturn(cliente).when(clienteService).buscarPorId(anyLong());
		doReturn(servicoDetalhado).when(servicoDetalhadoService).buscarPorId(anyLong());
		doReturn(avaliacao).when(converter).toDomain(any(AvaliacaoRepresentation.class));
		doReturn(avaliacaoRepresentation).when(converter).toRepresentation(any(Avaliacao.class));
		doReturn(avaliacaoRepresentationPage).when(converter).toRepresentationPage(any(Page.class));
		doReturn(singletonList(avaliacaoRepresentation)).when(converter).toRepresentationList(any(List.class));
		doReturn(avaliacaoPage).when(avaliacaoService).buscarAvaliacoesPorServicoId(anyLong(), any(Pageable.class));
		doAnswer(a -> {
			avaliacoes.add(avaliacao);
			return null;
		}).when(avaliacaoService).adicionarAvaliacao(any(Avaliacao.class));
	}

	@Test
	public void deve_criar_avaliacao() throws Exception {
		facade.adicionarAvaliacao(avaliacaoRepresentation, 1L);

		assertFalse(avaliacoes.isEmpty());
	}
	
	@Test
	public void deve_retornar_avaliacao_em_lista() throws Exception {
		facade.adicionarAvaliacao(avaliacaoRepresentation, 1L);

		var avaliacoes = facade.buscarAvaliacaoPorServico(1L, geraPageable(0, 5));
		
		assertFalse(avaliacoes.isEmpty());
	}
}
