package shopper.backend.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shopper.backend.mappers.UserMapper;
import shopper.backend.security.JwtAuthenticationFilter;
import shopper.backend.security.JwtAuthenticationProvider;
import shopper.backend.security.JwtService;
import shopper.backend.security.LogoutService;
import shopper.backend.services.UserService;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    // Lista de URL-uri care sunt excluse de la autentificare și permise fără autentificare.
    private static final String[] WHITE_LIST_URL = {
        "/auth/register",
        "/auth/login",
        "/api/products",
        "/api/products/**",
        "/generate/**",
        "/images/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
    };

    // Serviciile și filtrele necesare pentru configurarea securității.
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final LogoutService logoutService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Configurarea furnizorului de autentificare pentru tokenurile JWT.
    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtService, userMapper, userService);
    }

    // Configurarea filtrului principal de securitate.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
                    .requestMatchers(WHITE_LIST_URL).permitAll()
                    .requestMatchers("/api/v1/client/**").hasRole("CLIENT")
                    .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(jwtAuthenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(
                logout ->
                    logout.logoutUrl("/auth/logout")
                        .addLogoutHandler(logoutService)
                        .logoutSuccessHandler(
                            (request, response, authentication) -> SecurityContextHolder.clearContext()
                        )
            );
        return http.build();
    }
}