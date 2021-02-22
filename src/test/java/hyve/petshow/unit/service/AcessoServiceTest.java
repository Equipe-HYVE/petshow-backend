package hyve.petshow.unit.service;

import hyve.petshow.domain.Cliente;
import hyve.petshow.domain.Prestador;
import hyve.petshow.domain.VerificationToken;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AcessoRepository;
import hyve.petshow.repository.ClienteRepository;
import hyve.petshow.repository.PrestadorRepository;
import hyve.petshow.repository.VerificationTokenRepository;
import hyve.petshow.service.implementation.AcessoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static hyve.petshow.mock.ClienteMock.criaCliente;
import static hyve.petshow.mock.PrestadorMock.criaPrestador;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AcessoServiceTest {
	@Mock
	private AcessoRepository acessoRepository;
	@Mock
	private ClienteRepository clienteRepository;
	@Mock
	private PrestadorRepository prestadorRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private VerificationTokenRepository tokenRepository;
	@InjectMocks
	private AcessoServiceImpl service;

	private Cliente cliente = criaCliente();
	private Prestador prestador = criaPrestador();

	@BeforeEach
	public void init() {
		openMocks(this);

		doReturn(Optional.of(cliente)).when(acessoRepository).findByEmail(anyString());
		doReturn("token").when(passwordEncoder).encode(anyString());
		doReturn(cliente).when(clienteRepository).save(any(Cliente.class));
		doReturn(prestador).when(prestadorRepository).save(any(Prestador.class));
	}
	
	@Test
	public void deve_retornar_erro_por_nao_encontrado() {
		doReturn(Optional.empty()).when(acessoRepository).findByEmail(anyString());
		
		assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("teste@teste.com"));
	}
	
	@Test
	public void deve_retornar_usuario() {
		var conta = service.loadUserByUsername("teste@teste.com");

		assertNotNull(conta);
	}
	
	@Test
	public void deve_retornar_por_email() {
		var conta = service.buscarPorEmail("teste@teste.com");

		assertTrue(conta.isPresent());
	}
	
	@Test
	public void deve_retornar_vazio() {
		doReturn(Optional.empty()).when(acessoRepository).findByEmail(anyString());
		
		var conta = service.buscarPorEmail("teste@teste.com");

		assertTrue(conta.isEmpty());
	}
	
	@Test
	public void deve_adicionar_cliente() throws BusinessException {
		var actual = service.adicionarConta(cliente);

		assertNotNull(actual);
	}
	
	
	@Test
	public void deve_adicionar_prestador() throws BusinessException {
		var actual = service.adicionarConta(prestador);

		assertNotNull(actual);
	}

	@Test
	public void deve_retornar_erro_ao_adicionar_cliente() throws BusinessException {
		var clienteIncorreto = cliente;

		clienteIncorreto.setTipo(null);

		assertThrows(BusinessException.class, () -> service.adicionarConta(clienteIncorreto));
	}

	@Test
	public void deve_buscar_por_email() throws Exception {
		doReturn(Optional.of(prestador)).when(acessoRepository).findByEmail(anyString());

		var prestadorBuscado = service.buscarConta(prestador.getEmail());

		assertNotNull(prestadorBuscado);
	}

	@Test
	public void deve_retornar_erro_por_nao_encontrar_conta() {
		doReturn(Optional.empty()).when(acessoRepository).findByEmail(anyString());

		assertThrows(NotFoundException.class, () -> {
			service.buscarConta(prestador.getEmail());
		});
	}

	@Test
	public void deve_salvar_token() {
		doReturn(new VerificationToken()).when(tokenRepository).save(any(VerificationToken.class));

		var token = "kasljgaskld";
		var actual = service.criaTokenVerificacao(cliente, token);

		assertEquals(cliente, actual);
	}

	@Test
	public void deve_buscar_token() throws Exception {
		var tokenStr = "asldkjgaskdjlg";
		var token = new VerificationToken(cliente, tokenStr);

		doReturn(Optional.of(token)).when(tokenRepository).findByToken(anyString());

		var actual = service.buscarTokenVerificacao(tokenStr);

		assertEquals(token, actual);
	}

	@Test
	public void deve_retornar_erro_em_busca_por_token() {
		doReturn(Optional.empty()).when(tokenRepository).findByToken(anyString());

		assertThrows(NotFoundException.class, () -> {
			service.buscarTokenVerificacao("asdglkjsdg");
		});
	}

	@Test
	public void deve_ativar_conta() throws Exception {
		var token = "laskjasldkjgg";
		var clienteAlterado = cliente;

		clienteAlterado.getAuditoria().setFlagAtivo("N");

		var verificationToken = new VerificationToken(clienteAlterado, token);

		doReturn(Optional.of(clienteAlterado)).when(acessoRepository).findByEmail(anyString());
		doReturn(Optional.of(verificationToken)).when(tokenRepository).findByToken(anyString());
		doReturn(clienteAlterado).when(acessoRepository).save(any());

		service.ativaConta(token);

		assertTrue(clienteAlterado.isAtivo());
	}

	@Test
	public void deve_retornar_erro_por_conta_ja_ativa() {
		var token = "laskjasldkjgg";
		var clienteAlterado = cliente;

		clienteAlterado.getAuditoria().setFlagAtivo("S");

		var verificationToken = new VerificationToken(clienteAlterado, token);

		doReturn(Optional.of(clienteAlterado)).when(acessoRepository).findByEmail(anyString());
		doReturn(Optional.of(verificationToken)).when(tokenRepository).findByToken(anyString());

		assertThrows(BusinessException.class, () -> {
			service.ativaConta(token);
		});
	}
}
