package hyve.petshow.filter;

import hyve.petshow.service.port.AcessoService;
import hyve.petshow.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static hyve.petshow.util.NullUtils.isNotNull;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AcessoService acessoService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        String email = null;

        if(isNotNull(authHeader) && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            email = jwtUtils.extractUsername(token);
        }

        if(isNotNull(email) && SecurityContextHolder.getContext().getAuthentication() == null){
            var userDetails = acessoService.loadUserByUsername(email);

            if(jwtUtils.validateToken(token, userDetails)){
                var usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                var webAuthenticationDetailsSource = new WebAuthenticationDetailsSource().buildDetails(httpServletRequest);
                usernamePasswordAuthenticationToken.setDetails(webAuthenticationDetailsSource);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
