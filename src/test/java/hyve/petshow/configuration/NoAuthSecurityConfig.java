package hyve.petshow.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile("test")
@Order(1)
public class NoAuthSecurityConfig extends WebSecurityConfigurerAdapter {
	 @Override
	    protected void configure(HttpSecurity httpSecurity) throws Exception {
	        // Disable CSRF
	        httpSecurity.csrf().disable()
	                // Permit all requests without authentication
	                .authorizeRequests().anyRequest().permitAll();
	    }
}
