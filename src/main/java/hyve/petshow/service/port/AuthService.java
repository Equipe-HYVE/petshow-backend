package hyve.petshow.service.port;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface AuthService extends UserDetailsService {
}
