package com.pagamento.common.security;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração TLS para servidor Tomcat.
 * 
 * <p>Configura redirecionamento HTTP->HTTPS e portas personalizadas.</p>
 * 
 * @apiNote Configuração de rede segura
 */
@Configuration
public class TlsConfig {

    @Value("${server.http.port:8080}")
    private int httpPort;

    @Value("${server.port:8443}")
    private int httpsPort;

    /**
     * Customiza o servidor Tomcat para suporte TLS.
     * 
     * @return Customizador do servidor web
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        return factory -> {
            // Redirecionador HTTP->HTTPS
            factory.addAdditionalTomcatConnectors(createHttpRedirectConnector());
            
            // Configuração do conector HTTPS
            factory.addConnectorCustomizers(connector -> {
                connector.setScheme("https");
                connector.setSecure(true);
                connector.setPort(httpsPort);
                connector.setProperty("SSLEnabled", "true");
            });
        };
    }

    /**
     * Cria conector de redirecionamento HTTP.
     * 
     * @return Conector configurado
     */
    private Connector createHttpRedirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);
        return connector;
    }
}
