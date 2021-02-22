package hyve.petshow.controller.handler;

import hyve.petshow.service.port.AcessoService;
import hyve.petshow.util.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	@Value("${spring.app-url}")
	private String springAppUrl;
	
	@Autowired
	private AcessoService acessoService;
	@Autowired
	private JavaMailSender sender;
	
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		var conta = event.getConta();	
		var token = criaToken();
		acessoService.criaTokenVerificacao(conta, token);
		
		var emailDestino = conta.getLogin().getEmail();
		var assunto = "Confirmação de cadastro";
		var urlConfirmacao = event.getAppUrl() + "/confirmacao-registro?token=" + token;
		var message = "Obrigado por se cadastrar em Petshow!\nSegue link para ativação de sua conta";		
		var email = new SimpleMailMessage();
		email.setTo(emailDestino);
		email.setSubject(assunto);
		email.setFrom("no-reply@petshow.com");
		email.setText(message + "\r\n" + springAppUrl + urlConfirmacao );
		sender.send(email);
		
	}
	
	public String criaToken() {
		return randomUUID().toString();
	}
}
