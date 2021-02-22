package hyve.petshow.unit.service;

import hyve.petshow.controller.representation.MensagemRepresentation;
import hyve.petshow.domain.Cliente;
import hyve.petshow.repository.ClienteRepository;
import hyve.petshow.service.implementation.ClienteServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static hyve.petshow.mock.AuditoriaMock.auditoria;
import static hyve.petshow.mock.ClienteMock.criaCliente;
import static hyve.petshow.mock.ContaMock.contaCliente;
import static hyve.petshow.util.AuditoriaUtils.ATIVO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ClienteServiceTest {
	@Mock
	private ClienteRepository repository;
	@InjectMocks
	private ClienteServiceImpl service;

	private Cliente cliente = criaCliente();

	@BeforeEach
	public void init() {
		initMocks(this);

		doReturn(Optional.of(cliente)).when(repository).findById(1L);
		doReturn(Optional.of(cliente)).when(repository).findByEmail(anyString());
	}

	@AfterEach
	public void afterEach(){
		cliente.getAuditoria().setFlagAtivo(ATIVO);
	}

	@Test
	public void deve_atualizar_conta() throws Exception {
		var clienteRequest = cliente;
		clienteRequest.setTelefone("15152020");

		doReturn(clienteRequest).when(repository).save(clienteRequest);

		var actual = service.atualizarConta(1L, clienteRequest);

		assertEquals(actual.getTelefone(), cliente.getTelefone());
	}

	@Test
	public void deve_encontrar_conta_correta() throws Exception {
		var actual = service.buscarPorId(1L);

		assertEquals(cliente, actual);
	}

	@Test
	public void deve_retornar_excecao_por_pessoa_nao_encontrada() {
		assertThrows(Exception.class, () -> service.buscarPorId(5l));
	}

	@Test
	public void deve_remover_elemento_e_retornar_mensagem_sucesso() throws Exception {
		doReturn(cliente).when(repository).save(cliente);

		var mensagem = service.desativarConta(1L);

		assertAll(
				() -> assertEquals(MensagemRepresentation.MENSAGEM_SUCESSO, mensagem.getMensagem()),
				() -> assertTrue(mensagem.getSucesso()));
	}

	@Test
	public void deve_retornar_mensagem_de_erro() throws Exception {
		var clienteAtivo = new Cliente(contaCliente());//cliente();
		clienteAtivo.setAuditoria(auditoria(ATIVO));
		doReturn(clienteAtivo).when(repository).save(cliente);
		
		var removerConta = service.desativarConta(1L);

		assertEquals(MensagemRepresentation.MENSAGEM_FALHA, removerConta.getMensagem());
	}

	@Test
	public void deve_encontrar_por_email() {
		var cliente = service.buscarPorEmail("teste@teste");

		assertNotNull(cliente);
	}
}
