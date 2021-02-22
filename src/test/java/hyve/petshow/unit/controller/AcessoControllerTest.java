package hyve.petshow.unit.controller;

import hyve.petshow.controller.AcessoController;
import hyve.petshow.controller.converter.ContaConverter;
import hyve.petshow.controller.representation.ContaRepresentation;
import hyve.petshow.domain.Conta;
import hyve.petshow.domain.enums.TipoConta;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.service.port.AcessoService;
import hyve.petshow.util.JwtUtils;
import hyve.petshow.util.OnRegistrationCompleteEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static hyve.petshow.mock.ContaMock.contaCliente;
import static hyve.petshow.mock.ContaMock.contaRepresentation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AcessoControllerTest {
    @Mock
    private ContaConverter converter;
    @Mock
    private AcessoService service;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private AcessoController controller;

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private Conta conta = contaCliente();
    private ContaRepresentation contaRepresentation = contaRepresentation();
    private String token = "AS48QWE48Q94W89R4QW984AW8D4DW848E";

    @BeforeEach
    public void init() throws Exception {
        openMocks(this);

        doReturn(null).when(authenticationManager).authenticate(any(Authentication.class));
        doReturn(Optional.of(conta)).when(service).buscarPorEmail(anyString());
        doReturn(token).when(jwtUtils).generateToken(anyString(), anyLong(), any(TipoConta.class));
        doReturn(conta).when(converter).toDomain(any(ContaRepresentation.class));
        doReturn(conta).when(service).adicionarConta(any(Conta.class));
        doNothing().when(eventPublisher).publishEvent(any(OnRegistrationCompleteEvent.class));
        doReturn(conta).when(service).ativaConta(anyString());
        doReturn(conta).when(service).buscarConta(anyString());
    }

    @Test
    public void deve_retornar_token_apos_realizar_login() throws Exception {
    	doReturn(conta).when(service).buscarConta(anyString());
        var expected = ResponseEntity.ok(token);
        var actual = controller.realizarLogin(conta.getLogin());

        assertEquals(expected, actual);
    }

    @Test
    public void deve_lancar_business_exception_quando_erro_na_autenticacao_login() {
        doThrow(AuthenticationServiceException.class).when(authenticationManager).authenticate(any(Authentication.class));

        assertThrows(BusinessException.class, () -> controller.realizarLogin(conta.getLogin()));
    }

    @Test
    public void deve_lancar_not_found_exception_quando_nao_encontrar_conta() throws Exception {
        doThrow(NotFoundException.class).when(service).buscarConta(anyString());
        assertThrows(NotFoundException.class, () -> controller.realizarLogin(conta.getLogin()));
    }

    @Test
    public void deve_retornar_token_apos_realizar_cadastro() throws BusinessException {
        var expected = ResponseEntity.ok(token);

        doReturn(Optional.empty()).when(service).buscarPorEmail(anyString());

        var actual = controller.realizarCadastro(contaRepresentation, request);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_lancar_business_exception_caso_email_existente() {
        assertThrows(BusinessException.class, () -> controller.realizarCadastro(contaRepresentation, request));
    }

    @Test
    public void deve_confirmar_o_registro_de_uma_conta() throws Exception {
        var expected = ResponseEntity.ok(token);
        var actual = controller.confirmarRegistro(token);

        assertEquals(expected, actual);
    }

    @Test
    public void deve_reenviar_solicitacao_de_ativacao() throws Exception {
        var expected = ResponseEntity.ok("Reenviado");
        var actual = controller.reenviaSolicitacao("email", request);

        assertEquals(expected, actual);
    }
}
