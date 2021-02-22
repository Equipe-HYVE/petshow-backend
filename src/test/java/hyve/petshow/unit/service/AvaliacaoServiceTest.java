package hyve.petshow.unit.service;

import hyve.petshow.domain.Avaliacao;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AvaliacaoRepository;
import hyve.petshow.service.implementation.AvaliacaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacao;
import static hyve.petshow.mock.AvaliacaoMock.criaAvaliacaoList;
import static hyve.petshow.util.PagingAndSortingUtils.geraPageable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AvaliacaoServiceTest {
	@Mock
	private AvaliacaoRepository repository;

	@InjectMocks
	private AvaliacaoServiceImpl service;

	private Avaliacao avaliacao = criaAvaliacao();
	private List<Avaliacao> avaliacoes = criaAvaliacaoList();
	private Page<Avaliacao> avaliacaoPage = new PageImpl<>(avaliacoes);
	private Pageable pageable = geraPageable(0, 5);

	@BeforeEach
	public void init() {
		openMocks(this);

		doReturn(avaliacaoPage).when(repository).findByServicoAvaliadoId(anyLong(), any(Pageable.class));
		doReturn(Optional.of(avaliacao)).when(repository).findById(anyLong());
		doReturn(Optional.of(avaliacao)).when(repository).findByAgendamentoAvaliadoId(anyLong());
	}

	@Test
	public void deve_inserir_avaliacao() {
		doReturn(avaliacao).when(repository).save(any(Avaliacao.class));

		avaliacao = service.adicionarAvaliacao(avaliacao);

		assertNotNull(avaliacao);
	}

	@Test
	public void deve_retornar_avaliacoes() throws NotFoundException {
		var actual = service.buscarAvaliacoesPorServicoId(1L, pageable);

		assertEquals(avaliacaoPage, actual);
	}

	@Test
	public void deve_retornar_excecao_de_nenhuma_encontrada_por_servico() {
		doReturn(Page.empty()).when(repository).findByServicoAvaliadoId(anyLong(), any(Pageable.class));

		assertThrows(NotFoundException.class, () -> service.buscarAvaliacoesPorServicoId(10L, pageable));
	}
	
	@Test
	public void deve_retornar_algo_por_id() throws NotFoundException {
		var actual = service.buscarAvaliacaoPorId(1L);

		assertNotNull(actual);
	}
	
	@Test
	public void deve_retornar_excecao_por_nao_encontrar_por_id() {
		doReturn(Optional.empty()).when(repository).findById(anyLong());
		
		assertThrows(NotFoundException.class, () -> service.buscarAvaliacaoPorId(1L));
	}

	@Test
	public void deve_buscar_avaliacao_por_agendamento_id() throws Exception {
		var actual = service.buscarAvaliacaoPorAgendamentoId(1L);

		assertEquals(avaliacao, actual);
	}

	@Test
	public void deve_lancar_not_found_exception_ao_buscar_avaliacao_por_agendamento_id() throws Exception {
		doReturn(Optional.empty()).when(repository).findByAgendamentoAvaliadoId(anyLong());

		assertThrows(NotFoundException.class, () -> service.buscarAvaliacaoPorAgendamentoId(1L));
	}
}
