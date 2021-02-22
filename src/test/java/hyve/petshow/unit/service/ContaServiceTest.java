package hyve.petshow.unit.service;

import hyve.petshow.domain.Conta;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.mock.ClienteMock;
import hyve.petshow.repository.GenericContaRepository;
import hyve.petshow.service.implementation.ContaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static hyve.petshow.controller.representation.MensagemRepresentation.MENSAGEM_FALHA;
import static hyve.petshow.util.AuditoriaUtils.geraAuditoriaInsercao;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ContaServiceTest {
	@Mock
	private GenericContaRepository repository;
	
	@InjectMocks
	private ContaServiceImpl service;
	
	@BeforeEach
	public void init() {
		initMocks(this);
	}
	
	@Test
	public void deve_retornar_conta() throws Exception {
		// Given
		var conta = Optional.of(ClienteMock.criaCliente());
		
		
		doReturn(conta).when(repository).findById(anyLong());
		
		// When
		var contaDb = service.buscarPorId(1l);
		
		// Then
		assertNotNull(contaDb);
		assertEquals(conta.get().getId(), contaDb.getId());	
	}
	
	@Test
	public void deve_retornar_erro_por_conta_nao_encontrada() {
		// Given
		doReturn(Optional.empty()).when(repository).findById(anyLong());
		
		// Then
		assertThrows(NotFoundException.class, () -> {
			service.buscarPorId(1l);
		});
	}
	
	@Test
	public void deve_desativar_conta() throws Exception {
		// Given
		var conta = ClienteMock.criaCliente();
		doReturn(conta).when(repository).save(any());
		doReturn(Optional.of(conta)).when(repository).findById(anyLong());
		
		// When
		service.desativarConta(1l);
		
		// Then
		assertFalse(conta.isAtivo());
		
	}
	
	@Test
	public void deve_dar_problema_ao_desativar_conta() throws Exception {
		// Given
		var cliente = Mockito.mock(Conta.class);
		doReturn(cliente).when(repository).save(any());
		doReturn(Optional.of(cliente)).when(repository).findById(anyLong());
		doReturn(geraAuditoriaInsercao(Optional.of(1l))).when(cliente).getAuditoria();
		doReturn(true).when(cliente).isAtivo();
		// When
		var retorno = service.desativarConta(1l);
		// Then
		assertEquals(MENSAGEM_FALHA, retorno.getMensagem());
		
	}
	
	@Test
	public void deve_buscar_por_email() {
		// Given
		doReturn(Optional.of(ClienteMock.criaCliente())).when(repository).findByEmail(anyString());
		//When
		var busca = service.buscarPorEmail("sadfglksdjg");
		// Then
		assertTrue(busca.isPresent());
		
	}
	
	@Test
	public void deve_retornar_vazio_por_email() {
		// Given
		doReturn(Optional.empty()).when(repository).findByEmail(anyString());
		//When
		var busca = service.buscarPorEmail("sadfglksdjg");
		// Then
		assertTrue(busca.isEmpty());
	}
	
	@Test
	public void deve_atualizar_conta() throws Exception {
		// Given
		var cliente = ClienteMock.criaCliente();
		var clienteAtualizado = ClienteMock.criaCliente();
		doReturn(Optional.of(cliente)).when(repository).findById(anyLong());
		doReturn(cliente).when(repository).save(any());
		clienteAtualizado.setTelefone("398476239846");
		// When
		var atualizado = service.atualizarConta(1l, clienteAtualizado);
		assertEquals(clienteAtualizado.getTelefone(), atualizado.getTelefone());
	}

}
