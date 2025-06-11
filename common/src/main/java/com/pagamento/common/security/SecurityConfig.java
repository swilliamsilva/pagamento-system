/* ========================================================
* DOCUMENTAÇÃO OPENAPI - CONFIGURAÇÃO DE SEGURANÇA
# 
# Especificação para configuração de segurança da aplicação
# Classe: SecurityConfig
# 
#======================================== */
package com.pagamento.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Classe de configuração de segurança da aplicação
 * 
 * @Operation Configura políticas de segurança, autenticação e autorização
 * @Tag(name = "Security", description = "Configurações de segurança da API")
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuração principal da cadeia de filtros de segurança
     * 
     * @Operation Configura as regras de acesso aos endpoints
     * @param http Configuração HTTP de segurança
     * @return SecurityFilterChain cadeia de filtros configurada
     * @throws Exception em caso de erro na configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    new AntPathRequestMatcher("/api/public/**"),
                    new AntPathRequestMatcher("/actuator/health"),
                    new AntPathRequestMatcher("/swagger-ui/**"),
                    new AntPathRequestMatcher("/v3/api-docs/**")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());
        
        return http.build();
    }
}


 /**
  * Fluxo de Processamento
Estágio	Descrição
Entrada	Requisição HTTP recebida pelo servidor
Processo	1. Verifica rota contra as regras configuradas
2. Aplica filtros de segurança
3. Valida token JWT (para rotas autenticadas)
Saída	- Acesso permitido (200)
- Não autorizado (401)
- Proibido (403)
Relacionamentos com Outras Classes
Classe	Tipo de Relação	Descrição
HttpSecurity	Dependência	Classe do Spring Security para configuração de regras
SecurityFilterChain	Retorno	Interface que representa a cadeia de filtros de segurança
OAuth2ResourceServer	Configuração	Configuração do servidor de recursos OAuth2
JwtDecoder	Dependência implícita	Responsável por decodificar tokens JWT
Passo a Passo de Funcionamento

    Inicialização:

        Spring detecta a anotação @EnableWebSecurity

        Cria a cadeia de filtros definida no método securityFilterChain

    Processamento de Requisições:
    Diagram
    Code

graph TD
  A[Requisição Entrante] --> B{É rota pública?}
  B -->|Sim| C[Permite acesso]
  B -->|Não| D{Token JWT válido?}
  D -->|Sim| E[Permite acesso]
  D -->|Não| F[Retorna 401/403]

Configurações Específicas:

    Desabilita CSRF para APIs stateless

    Configura OAuth2 Resource Server para autenticação JWT*/
