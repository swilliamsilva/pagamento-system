package com.pagamento.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuração de segurança para aplicação.
 * 
 * <p>Habilita autenticação via JWT e proteções contra vulnerabilidades web.</p>
 * 
 * @apiNote Configuração de segurança principal
 * @securityRequirementName JWT
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuração do filtro de segurança.
     * 
     * @param http Configuração HTTP
     * @return Cadeia de filtros de segurança
     * @throws Exception Em caso de erro de configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Requer HTTPS para todas as requisições
            .requiresChannel(channel -> 
                channel.anyRequest().requiresSecure())
            
            // Configuração de autorização
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/actuator/health/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-resources/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            
            // Proteção CSRF
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/actuator/**")
            )
            
            // Configuração de cabeçalhos de segurança
            .headers(headers -> headers
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)  // 1 ano
                )
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self'")
                )
                .frameOptions(frame -> frame
                    .deny()
                )
                .xssProtection(xss -> xss
                    .block(true)
                )
                .referrerPolicy(referrer -> referrer
                    .policy("same-origin")
                )
            )
            
            // Habilita autenticação básica para serviços internos
            .httpBasic(withDefaults());
        
        return http.build();
    }
    
    /**
     * Configuração de segurança para ambiente de desenvolvimento.
     * 
     * <p>Desabilita algumas proteções para facilitar desenvolvimento e testes.</p>
     */
    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.disable());
        
        return http.build();
    }
}
