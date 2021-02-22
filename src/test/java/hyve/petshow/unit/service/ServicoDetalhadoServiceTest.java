package hyve.petshow.unit.service;

import hyve.petshow.controller.filter.ServicoDetalhadoFilter;
import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Servico;
import hyve.petshow.domain.ServicoDetalhado;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.ServicoDetalhadoRepository;
import hyve.petshow.service.implementation.ServicoDetalhadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static hyve.petshow.mock.ServicoDetalhadoMock.criaServicoDetalhado;
import static hyve.petshow.util.PagingAndSortingUtils.geraPageable;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ServicoDetalhadoServiceTest {
	@Mock
	private ServicoDetalhadoRepository repository;

	@InjectMocks
	private ServicoDetalhadoServiceImpl service;

	private ServicoDetalhado servicoDetalhado = criaServicoDetalhado();
	private List<ServicoDetalhado> servicoDetalhadoList = singletonList(servicoDetalhado);
	private Page<ServicoDetalhado> servicoDetalhadoPage = new PageImpl<>(servicoDetalhadoList);
	private Pageable pageable = geraPageable(0, 5);
	private ServicoDetalhadoFilter filter = new ServicoDetalhadoFilter();

	@BeforeEach
    public void init() {
		openMocks(this);

		doReturn(servicoDetalhado).when(repository).save(any(ServicoDetalhado.class));
		doReturn(Optional.of(servicoDetalhado)).when(repository).findById(1L);
		doReturn(servicoDetalhadoList).when(repository).findAll();
		doReturn(servicoDetalhadoPage).when(repository).findByPrestadorId(anyLong(), any(Pageable.class));
		doReturn(servicoDetalhadoPage).when(repository).findAll(any(Specification.class), any(Pageable.class));
		doReturn(Optional.of(servicoDetalhado)).when(repository).findByIdAndPrestadorId(anyLong(), anyLong());
		doNothing().when(repository).delete(any(ServicoDetalhado.class));
	}
    
    @Test
	public void deve_inserir_servico_detalhado() {
		var actual = service.adicionarServicoDetalhado(servicoDetalhado);
		
		assertAll(() -> assertTrue(repository.findAll().contains(actual)),
				  () -> assertNotNull(actual));
	}
    
    @Test
	public void deve_atualizar_servico_detalhado() throws Exception {
		var servicoDetalhadoRequest = servicoDetalhado;
		var tipo = new Servico();
		tipo.setNome("Banho");
		servicoDetalhadoRequest.setTipo(tipo);

		doReturn(servicoDetalhadoRequest).when(repository).save(servicoDetalhadoRequest);

		var actual = service.atualizarServicoDetalhado(1L, 1L, servicoDetalhadoRequest);

		assertEquals(actual.getTipo().getNome(), servicoDetalhado.getTipo().getNome());
	}

	@Test
	public void deve_retornar_mensagem_sucesso_ao_remover_elemento() throws Exception {
		var mensagem = service.removerServicoDetalhado(1L, 1L);

		doReturn(FALSE).when(repository).existsById(1L);

		assertAll(
				() -> assertEquals(MensagemRepresentation.MENSAGEM_SUCESSO, mensagem.getMensagem()),
				() -> assertTrue(mensagem.getSucesso()));
	}

	@Test
	public void deve_retornar_por_tipo() throws NotFoundException {
		var lista = service.buscarServicosDetalhadosPorTipoServico(pageable, filter);

		assertFalse(lista.isEmpty());
	}
	
	@Test
	public void deve_retornar_excecao_nao_encontrado() {
		doReturn(Optional.empty()).when(repository).findById(anyLong());

		assertThrows(NotFoundException.class, () -> service.buscarPorId(1L));
	}
	
	@Test
	public void deve_retornar_por_id_prestador() throws NotFoundException {
		var lista = service.buscarPorPrestadorId(1L, pageable);

		assertFalse(lista.isEmpty());
	}
	
	@Test
	public void deve_retornar_erro_por_nao_encontrado() {
		doReturn(Page.empty()).when(repository).findByPrestadorId(anyLong(), any(Pageable.class));
		
		assertThrows(NotFoundException.class, () -> service.buscarPorPrestadorId(1L, pageable));
	}
	
	@Test
	public void deve_retornar_servico_por_prestador_e_id() throws NotFoundException {
		var servico = service.buscarPorPrestadorIdEServicoId(1L, 1L);

		assertNotNull(servico);
	}
	
	@Test
	public void deve_retornar_excecao_por_nao_encontrar_por_id_e_prestador() {
		doReturn(Optional.empty()).when(repository).findByIdAndPrestadorId(anyLong(), anyLong());
		
		assertThrows(NotFoundException.class, () -> service.buscarPorPrestadorIdEServicoId(1L, 1L));
	}
	
	@Test
	public void deve_retornar_excecao_por_donos_diferentes_em_atualizacao() {
		var servicoRequest = criaServicoDetalhado();

		assertThrows(BusinessException.class, () -> service.atualizarServicoDetalhado(1L, 2L, servicoRequest));
	}
	
	@Test
	public void deve_retornar_excecao_por_donos_diferentes_em_delecao() {
		assertThrows(BusinessException.class, () -> service.removerServicoDetalhado(1L, 2L));
	}
	
	@Test
	public void deve_retornar_mensagem_de_falha_em_delecao() throws BusinessException, NotFoundException {
		doReturn(TRUE).when(repository).existsById(anyLong());

		var mensagem = service.removerServicoDetalhado(servicoDetalhado.getId(), servicoDetalhado.getPrestadorId());

		assertEquals(MensagemRepresentation.MENSAGEM_FALHA, mensagem.getMensagem());
	}
	
	@Test
	public void deve_retornar_dois_servicos() {
		var servicosDetalhados = new ArrayList<ServicoDetalhado>() {
			private static final long serialVersionUID = 1L;

		{
			add(new ServicoDetalhado());
			add(new ServicoDetalhado());
		}};
		
		doReturn(servicosDetalhados).when(repository).findAllById(anyIterable());
		
		assertEquals(servicosDetalhados, service.buscarServicosDetalhadosPorIds(Arrays.asList(new Long[] {1l,2l})));
	}

	@Test
	public void deve_salvar_com_lista_vazia_de_adicionais_ao_nenhum_ser_informado_ao_adicionar_servico_detalhado(){
		var servicoDetalhadoRequest = criaServicoDetalhado();

		servicoDetalhadoRequest.setAdicionais(Collections.emptyList());

		var actual = service.adicionarServicoDetalhado(servicoDetalhadoRequest);

		assertAll(() -> assertTrue(repository.findAll().contains(actual)),
				() -> assertNotNull(actual));
	}
}
