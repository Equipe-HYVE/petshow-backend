package hyve.petshow.controller;

import hyve.petshow.controller.converter.ContaConverter;
import hyve.petshow.controller.representation.ContaRepresentation;
import hyve.petshow.controller.representation.PrestadorRepresentation;
import hyve.petshow.domain.Conta;
import hyve.petshow.domain.embeddables.Login;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.facade.AcessoFacade;
import hyve.petshow.service.port.AcessoService;
import hyve.petshow.util.JwtUtils;
import hyve.petshow.util.OnRegistrationCompleteEvent;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static hyve.petshow.util.LogUtils.Messages.INFO_REQUEST_CONTROLLER_BODY_MESSAGE;

@Slf4j
@RestController
@RequestMapping("/acesso")
@OpenAPIDefinition(info = @Info(title = "API de acesso à aplicação",
        description = "API utilizada para a realização de login e cadastro"))
public class AcessoController {
    private static final String EMAIL_JA_CADASTRADO = "EMAIL_JA_CADASTRADO";
    private static final String USUARIO_SENHA_INCORRETO = "USUARIO_SENHA_INCORRETO"; //Erro durante a autenticação, usuário ou senha incorretos
    
	@Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AcessoService acessoService;
    @Autowired
    private AcessoFacade acessoFacade;
    @Autowired
    private ContaConverter contaConverter;
    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @Operation(summary = "Realiza o login, gerando um token para ser utilizado nas demais APIs.")
    @PostMapping("/login")
    public ResponseEntity<String> realizarLogin(
            @Parameter(description = "Objeto utilizado para realizar o login.")
            @RequestBody Login login) throws Exception {
        try {
            log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/acesso/login", login.getEmail());
            realizarAutenticacao(login);
            var token = gerarToken(login.getEmail());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            log.error("{}, mensagem: {}, causa: {}", USUARIO_SENHA_INCORRETO, e.getMessage(), e.getCause());
            throw new BusinessException(USUARIO_SENHA_INCORRETO);
        }
    }

    @Operation(summary = "Realiza o cadastro, salvando o usuário no sistema e " +
            "gerando um token para ser utilizado nas demais APIs.")
    @PostMapping("/cadastro")
    public ResponseEntity<String> realizarCadastro(
            @Parameter(description = "Objeto da conta que será cadastrada.")
            @RequestBody ContaRepresentation contaRepresentation,
            @Parameter(description = "Requisição")
            HttpServletRequest request) throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/acesso/cadastro", contaRepresentation.getLogin().getEmail());
        verificarEmailExistente(contaRepresentation.getLogin().getEmail());
        var conta = adicionarConta(contaRepresentation);
        var token = gerarToken(conta);
        var appUrl = request.getContextPath();

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(conta, request.getLocale(), appUrl));

        return ResponseEntity.ok(token);
    }
    
    @Operation(summary = "Realiza cadastro de prestador com empresa")
	@PostMapping("/cadastro/empresa")
	public ResponseEntity<String> realizarCadastro(
			@Parameter(description = "Prestador")
			@RequestBody PrestadorRepresentation representation,
			@Parameter(description = "Requisição")
			HttpServletRequest request) throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/acesso/cadastro/empresa", representation.getLogin().getEmail());
    	verificarEmailExistente(representation.getLogin().getEmail());
    	var prestador = acessoFacade.salvaPrestador(representation);
    	var domain = contaConverter.toDomain(prestador);
		var token = gerarToken(domain);
    	var appUrl = request.getContextPath();
    	
    	eventPublisher.publishEvent(new OnRegistrationCompleteEvent(domain, request.getLocale(), appUrl));
    	return ResponseEntity.ok(token);
		
	}

    @GetMapping("/ativar")
    public ResponseEntity<String> confirmarRegistro(@RequestParam("token") String tokenVerificadcao) throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/acesso/ativar", tokenVerificadcao);
        var conta = acessoService.ativaConta(tokenVerificadcao);
        var tokenRetorno = gerarToken(conta.getLogin().getEmail());
        return ResponseEntity.ok(tokenRetorno);
    }

    @Operation(summary = "Reenvia token de ativação de conta")
    @PostMapping("/reenvia-ativacao")
    public ResponseEntity<String> reenviaSolicitacao(
            @Parameter(description = "Email para enviar a nova solicitação")
            @RequestBody String email,
            @Parameter(description = "Requisição")
                    HttpServletRequest request) throws Exception {
        log.info(INFO_REQUEST_CONTROLLER_BODY_MESSAGE, "/acesso/reenvia-ativacao", email);
        var conta = acessoService.buscarConta(email);
        var appUrl = request.getContextPath();
        var event = new OnRegistrationCompleteEvent(conta, request.getLocale(), appUrl);
        eventPublisher.publishEvent(event);
        return ResponseEntity.ok("Reenviado");
    }

    private void realizarAutenticacao(Login login) throws AuthenticationException{
        var token = new UsernamePasswordAuthenticationToken(login.getEmail(), login.getSenha());
        authenticationManager.authenticate(token);
    }

    private String gerarToken(String email) throws Exception {
    	var conta = acessoService.buscarConta(email);
        return jwtUtils.generateToken(email, conta.getId(), conta.getTipo());
    }

    private String gerarToken(Conta conta) {
        return jwtUtils.generateToken(conta.getLogin().getEmail(), conta.getId(), conta.getTipo());
    }

    private void verificarEmailExistente(String email) throws BusinessException {
        if(acessoService.buscarPorEmail(email).isPresent()){
            throw new BusinessException(EMAIL_JA_CADASTRADO);//Email já cadastrado no sistema
        }
    }

	private Conta adicionarConta(ContaRepresentation contaRepresentation) throws Exception {
		var conta = acessoFacade.salvaConta(contaRepresentation);
		return contaConverter.toDomain(conta);
	}
}
