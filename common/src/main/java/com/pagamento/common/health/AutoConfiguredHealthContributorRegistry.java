/* ========================================================
# Classe: AutoConfiguredHealthContributorRegistry
# Módulo: pagamento-common-health
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Website: simuleagora.com
# Tecnologias: Java 8, Spring Boot 2.7, Maven - Junho 2025
# ======================================================== */

package com.pagamento.common.health;

import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Component para registro e gerenciamento dinâmico de HealthContributors
 * 
 * <p>Implementação customizada de HealthContributorRegistry que permite:</p>
 * <ul>
 *   <li>Registro automático de componentes de saúde</li>
 *   <li>Gerenciamento dinâmico de verificações de saúde</li>
 *   <li>Integração com Spring Boot Actuator</li>
 * </ul>
 * 
 * <h2>Fluxo Principal</h2>
 * <ol>
 *   <li><b>Entrada:</b>
 *     <ul>
 *       <li>Mapa de HealthContributors no construtor</li>
 *       <li>Chamadas de registro/desregistro via métodos da interface</li>
 *     </ul>
 *   </li>
 *   <li><b>Processamento:</b>
 *     <ul>
 *       <li>Armazenamento em mapa thread-safe</li>
 *       <li>Delegação para os contribuidores registrados</li>
 *     </ul>
 *   </li>
 *   <li><b>Saída:</b>
 *     <ul>
 *       <li>Dados de saúde agregados</li>
 *       <li>Iteração sobre contribuidores registrados</li>
 *     </ul>
 *   </li>
 * </ol>
 * 
 * <h2>Relação com Outras Classes</h2>
 * <table>
 *   <tr><th>Classe</th><th>Relação</th></tr>
 *   <tr><td>HealthContributor</td><td>Interface base para contribuidores</td></tr>
 *   <tr><td>HealthIndicator</td><td>Tipo específico de contribuidor</td></tr>
 *   <tr><td>CompositeHealthContributor</td><td>Para agrupamento de contribuidores</td></tr>
 *   <tr><td>HealthEndpoint</td><td>Endpoint do Actuator que consome este registry</td></tr>
 * </table>
 * 
 * @see <a href="https://docs.spring.io/spring-boot/docs/2.7.x/reference/html/actuator.html#actuator.endpoints.health">Spring Boot Health Docs</a>
 */
@Component
public class AutoConfiguredHealthContributorRegistry implements HealthContributorRegistry {

    private final Map<String, HealthContributor> contributors = new ConcurrentHashMap<>();

    /**
     * Constrói o registry com contribuidores iniciais
     * 
     * @param initialContributors Mapa de contribuidores para registro inicial
     */
    public AutoConfiguredHealthContributorRegistry(Map<String, HealthContributor> initialContributors) {
        if (initialContributors != null) {
            this.contributors.putAll(initialContributors);
        }
    }

    @Override
    public void registerContributor(String name, HealthContributor contributor) {
        if (name == null || contributor == null) {
            throw new IllegalArgumentException("Name and contributor cannot be null");
        }
        contributors.put(name, contributor);
    }

    @Override
    public HealthContributor unregisterContributor(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        return contributors.remove(name);
    }

    @Override
    public HealthContributor getContributor(String name) {
        return contributors.get(name);
    }

    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        return contributors.entrySet().stream()
            .map(entry -> NamedContributor.of(entry.getKey(), entry.getValue()))
            .iterator();
    }
}