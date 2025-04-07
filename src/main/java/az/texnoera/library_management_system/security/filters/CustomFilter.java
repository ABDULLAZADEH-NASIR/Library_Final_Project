package az.texnoera.library_management_system.security.filters;


import az.texnoera.library_management_system.security.utilities.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {

            String jwtToken = token.substring(7);  // "Bearer "-dən sonra olan tokeni alırıq

            if (jwtUtils.validateJwtToken(jwtToken)) {
                String username = jwtUtils.parseJwtToken(jwtToken).getSubject();  // Tokenin subjecti username (email) olacaq

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // E-poçt ilə istifadəçi məlumatlarını yükləyirik

                // İstifadəçi məlumatları doğrulandı, securityContext yaradılır
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        try {
            filterChain.doFilter(request, response);  // Filtrdən keçmək
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
