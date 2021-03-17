package hyve.petshow;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static hyve.petshow.util.UrlUtils.*;

@EnableAsync
@SpringBootApplication
public class Application {
	@Value("${spring.mail.host}")
	private String smtpHost;
	@Value("${spring.mail.username}")
	private String mail;
	@Value("${spring.mail.password}")
	private String password;
	@Value("${mercado-pago.access.token}")
	private String mercadoPagoAccessToken;
	@Value("${mercado-pago.access.url-base-front}")
	private String urlBaseFront;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    public WebMvcConfigurer corsConfigurer() {
    	return new WebMvcConfigurer() {
    		@Override
    		public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedHeaders("*")
						.allowedOrigins(URL_API_LOCAL, URL_API_LOCAL_DOCKER, URL_API_PROD)
						.allowedMethods("*")
						.allowCredentials(true);
			}
		};
    }
    
    @Bean
	public JavaMailSender getJavaMailSender() {
	    var mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(smtpHost);
	    mailSender.setPort(587);
	    
	    mailSender.setUsername(mail);
	    mailSender.setPassword(password);
	    
	    var props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}

	@Bean
	public void setPaymentToken() throws MPConfException {
		MercadoPago.SDK.setAccessToken(mercadoPagoAccessToken);
	}

	public String getUrlBaseFront(){
		return this.urlBaseFront;
	}
}
