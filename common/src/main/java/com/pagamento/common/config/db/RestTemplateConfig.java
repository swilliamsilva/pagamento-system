package com.pagamento.common.config.db;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * ========================================================
 * CONFIGURAÇÃO DO REST TEMPLATE
 * 
 * Classe de configuração para o cliente HTTP RestTemplate
 * Módulo: pagamento-common
 * Autor: William Silva
 * Contato: williamsilva.codigo@gmail.com
 * Site: simuleagora.com
 * ========================================================
 */
//@Configuration
public class RestTemplateConfig {
    
    /**
     * Cria e configura um RestTemplate com timeout
     * 
     * @return RestTemplate configurado
     * 
     * @bean restTemplate
     * @description Configura o cliente HTTP com timeout de 3 segundos
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}