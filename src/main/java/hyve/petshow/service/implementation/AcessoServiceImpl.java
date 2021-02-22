package hyve.petshow.service.implementation;

import hyve.petshow.domain.Cliente;
import hyve.petshow.domain.Conta;
import hyve.petshow.domain.Prestador;
import hyve.petshow.domain.VerificationToken;
import hyve.petshow.domain.embeddables.Login;
import hyve.petshow.domain.enums.TipoConta;
import hyve.petshow.exceptions.BusinessException;
import hyve.petshow.exceptions.NotFoundException;
import hyve.petshow.repository.AcessoRepository;
import hyve.petshow.repository.ClienteRepository;
import hyve.petshow.repository.PrestadorRepository;
import hyve.petshow.repository.VerificationTokenRepository;
import hyve.petshow.service.port.AcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static hyve.petshow.util.AuditoriaUtils.*;

@Service
public class AcessoServiceImpl implements AcessoService {
	private static final String CONTA_JA_ATIVA = "CONTA_JA_ATIVA";//"Conta já ativa";
	private static final String TOKEN_NAO_ENCONTRADO = "TOKEN_NAO_ENCONTRADO";//Token informado não encontrado
	private static final String TIPO_DE_CLIENTE_INEXISTENTE = "TIPO_DE_CLIENTE_INEXISTENTE";//Tipo de cliente inexistente
	private static final String CONTA_NAO_ENCONTRADA = "CONTA_NAO_ENCONTRADA"; //Conta não encontrada
	
	@Autowired
    private AcessoRepository acessoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PrestadorRepository prestadorRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var conta = acessoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(CONTA_NAO_ENCONTRADA));
        var login = conta.getLogin();
        var user = new User(login.getEmail(), login.getSenha(), new ArrayList<>());
        return user;
    }

    @Override
    public Optional<Conta> buscarPorEmail(String email) {
        var conta = acessoRepository.findByEmail(email);
        return conta;
    }

    @Override
    public Conta adicionarConta(Conta conta) throws BusinessException {
        var tipoConta = conta.getTipo();
        criptografarSenha(conta.getLogin());

        conta.setAuditoria(geraAuditoriaInsercaoConta(Optional.empty()));

        if(TipoConta.CLIENTE.equals(tipoConta)){
            var cliente = new Cliente(conta);
            conta = clienteRepository.save(cliente);
        } else if(TipoConta.PRESTADOR_AUTONOMO.equals(tipoConta)){
            var prestador = new Prestador(conta);
            conta = prestadorRepository.save(prestador);
        } else {
            throw new BusinessException(TIPO_DE_CLIENTE_INEXISTENTE);
        }

        return conta;
    }

    private void criptografarSenha(Login login){
        var senha = login.getSenha();
        login.setSenha(passwordEncoder.encode(senha));
    }

    public Conta buscarConta(String email) throws Exception {
    	return buscarPorEmail(email)
                .orElseThrow(() -> new NotFoundException(CONTA_NAO_ENCONTRADA));
    }

	@Override
	public Conta criaTokenVerificacao(Conta conta, String token) {
		var verificationToken = new VerificationToken(conta, token);
		tokenRepository.save(verificationToken);
		return conta;
	}

	@Override
	public VerificationToken buscarTokenVerificacao(String tokenVerificadcao) throws Exception {
		return tokenRepository.findByToken(tokenVerificadcao)
                .orElseThrow(() -> new NotFoundException(TOKEN_NAO_ENCONTRADO));
	}

	@Override
	public Conta ativaConta(String token) throws Exception {
		var tokenVerificacao = buscarTokenVerificacao(token);
		var conta = buscarConta(tokenVerificacao.getConta().getEmail());
		if(conta.isAtivo()) {
			throw new BusinessException(CONTA_JA_ATIVA);
		}
		conta.setAuditoria(atualizaAuditoria(conta.getAuditoria(), ATIVO));
		return acessoRepository.save(conta);

	}
}
