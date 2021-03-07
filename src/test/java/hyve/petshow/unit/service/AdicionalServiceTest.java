package hyve.petshow.unit.service;

import hyve.petshow.domain.Adicional;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AdicionalRepository;
import hyve.petshow.service.implementation.AdicionalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static hyve.petshow.mock.AdicionalMock.criaAdicional;
import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AdicionalServiceTest {
	@Mock
	private AdicionalRepository repository;
	@InjectMocks
	private AdicionalServiceImpl service;

	@BeforeEach
	public void init() {
		openMocks(this);
	}

	@Test
	public void deve_lista_de_objetos() throws Exception {
		// Given
		var esperado = new ArrayList<Adicional>() {
			private static final long serialVersionUID = 1L;

			{
				add(criaAdicional(1l));
				add(criaAdicional(2l));
			}
		};

		doReturn(esperado).when(repository).findByServicoDetalhadoIdAndAuditoriaFlagAtivo(anyLong(), anyString());

		// Then
		assertEquals(esperado, service.buscarAtivosPorServicoDetalhado(1l));
	}

	@Test
	public void deve_retornar_erro_por_lista_de_adicionais_vazia() {
		// Given
		doReturn(Collections.emptyList()).when(repository).findByServicoDetalhadoIdAndAuditoriaFlagAtivo(anyLong(), anyString());

		// Then
		assertThrows(NotFoundException.class, () -> {
			service.buscarAtivosPorServicoDetalhado(1l);
		});
	}

	@Test
	public void deve_criar_adicional() {
		var mockDb = new ArrayList<Adicional>();

		doAnswer(mock -> {
			var adicional = (Adicional) mock.getArgument(0);
			adicional.setId((long) (mockDb.size() + 1));
			mockDb.add(adicional);
			return adicional;
		}).when(repository).save(any());

		var adicional = criaAdicional(1l);
		service.criarAdicional(adicional, 1L);

		assertTrue(mockDb.contains(adicional));
	}

	@Test
	public void deve_adicionar_auditoria_para_adicional() {
		var adicional = criaAdicional(1l);
		doReturn(adicional).when(repository).save(any());

		service.criarAdicional(adicional, 1L);

		assertNotNull(adicional.getAuditoria());
	}

	@Test
	public void deve_adicionar_auditoria_ativa() {
		var adicional = criaAdicional(1l);
		doReturn(adicional).when(repository).save(any());

		service.criarAdicional(adicional, 1L);

		assertTrue(adicional.getAuditoria().isAtivo());
	}

	@Test
	public void deve_retornar_por_id() throws Exception {
		var adicional = criaAdicional(1l);
		doReturn(Optional.ofNullable(adicional)).when(repository).findById(anyLong());

		assertEquals(adicional, service.buscarPorId(1l));
	}

	@Test
	public void deve_disparar_excecao_por_nao_encontrar_em_busca_por_id() {
		doReturn(Optional.empty()).when(repository).findById(anyLong());
		assertThrows(NotFoundException.class, () -> {
			service.buscarPorId(1l);
		});
	}
	
	@Test
	public void deve_atualizar_adicional() throws Exception {
		var adicional = criaAdicional(1l);
		adicional.setAuditoria(geraAuditoriaInsercao(Optional.empty()));
		var esperado = criaAdicional(1l);
		esperado.setNome("Teste2");
		esperado.setDescricao("Teste2");
		
		doReturn(Optional.ofNullable(adicional)).when(repository).findById(anyLong());
		doReturn(adicional).when(repository).save(any());
		
		service.atualizarAdicional(adicional.getId(), esperado);
		assertEquals(esperado.getNome(), adicional.getNome());
		assertEquals(esperado.getDescricao(), adicional.getDescricao());
	}
	
	@Test
	public void deve_desativar_adicional() throws Exception {
		var auditoria = geraAuditoriaInsercao(Optional.empty());
		auditoria.setFlagAtivo("S");
		var adicional = criaAdicional(1l);
		adicional.setAuditoria(auditoria);
		doReturn(Optional.ofNullable(adicional)).when(repository).findById(anyLong());
		doReturn(adicional).when(repository).save(any());
		
		service.desativarAdicional(adicional.getId(), adicional.getServicoDetalhadoId(), Boolean.FALSE);
		
		assertFalse(adicional.getAuditoria().isAtivo());
	}

	@Test
	public void deve_retornar_erro_quando_adicional_buscado_estiver_desativado() throws Exception {
		var adicional = criaAdicional(1L);

		adicional.getAuditoria().setFlagAtivo("N");

		doReturn(Optional.of(adicional)).when(repository).findById(anyLong());

		assertThrows(NotFoundException.class,
				() -> service.buscarPorId(1L));
	}

	@Test
	public void deve_retornar_varios_adicionais_ao_buscar_por_seus_ids() throws Exception {
		var expected = Arrays.asList(criaAdicional(1L));

		doReturn(expected).when(repository)
				.findByServicoDetalhadoIdAndIdInAndAuditoriaFlagAtivo(anyLong(), anyList(), anyString());

		var actual = service.buscarAdicionaisPorIds(1L, Arrays.asList(1L, 2L));

		assertEquals(expected, actual);
	}

	@Test
	public void deve_lancar_not_found_exception_ao_nao_encontrar_adicionais(){
		var adicionais = Collections.emptyList();

		doReturn(adicionais).when(repository)
				.findByServicoDetalhadoIdAndIdInAndAuditoriaFlagAtivo(anyLong(), anyList(), anyString());

		assertThrows(NotFoundException.class,
				() -> service.buscarAdicionaisPorIds(1L, Arrays.asList(1L, 2L)));
	}

	@Test
	public void deve_lancar_business_exception_ao_id_servico_divergir_ao_atualizar(){
		var adicionalBuscado = criaAdicional(1L);
		var adicionalParam = criaAdicional(1L);

		adicionalParam.setServicoDetalhadoId(2L);

		doReturn(Optional.of(adicionalBuscado)).when(repository).findById(1L);

		assertThrows(BusinessException.class, () ->
				service.atualizarAdicional(1L, adicionalParam));
	}

	@Test
	public void deve_lancar_business_exception_ao_id_servico_divergir_ao_deletar(){
		var adicionalBuscado = criaAdicional(1L);

		doReturn(Optional.of(adicionalBuscado)).when(repository).findById(1L);

		assertThrows(BusinessException.class, () ->
				service.desativarAdicional(1L, 2L, Boolean.FALSE));
	}

	@Test
	public void deve_criar_multiplos_adicionais(){
		var expected = Arrays.asList(criaAdicional(1L));

		doReturn(expected.get(0)).when(repository).save(any(Adicional.class));

		var actual = service.criarAdicionais(expected, 1L);

		assertEquals(expected, actual);
	}

	@Test
	public void deve_retornar_lista_vazia_ao_tentar_criar_multiplos_adicionais(){
		var expected = new ArrayList<Adicional>();

		var actual = service.criarAdicionais(expected, 1L);

		assertEquals(expected, actual);
	}
}
