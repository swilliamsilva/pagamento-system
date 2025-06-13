package com.pagamento.common.health;

/*
# Classe: HealthContributorRegistry
# Módulo: Health Check Management
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring Boot 2.7, Maven
# Data: Junho de 2025
# ======================================================== */



import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Tag(
    name = "Health Registry",
    description = "Registro e gerenciamento centralizado de verificadores de saúde"
)
public class HealthContributorRegistry implements HealthContributor, Iterable<NamedContributor<HealthContributor>> {

    private final Map<String, HealthContributor> contributors = new HashMap<>();

    /**
     * Registra um novo contribuidor de health check.
     * 
     * @param name Nome identificador do contribuidor
     * @param contributor Instância do HealthContributor
     */
    public void registerContributor(String name, HealthContributor contributor) {
        contributors.put(name, contributor);
    }

    /**
     * Obtém um contribuidor específico pelo nome.
     * 
     * @param name Nome do contribuidor
     * @return Instância do HealthContributor
     */
    public HealthContributor getContributor(String name) {
        return contributors.get(name);
    }

    /**
     * Permite iteração sobre os contribuidores registrados.
     * 
     * @return Iterator de NamedContributor
     */
    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        return contributors.entrySet().stream()
            .map(entry -> NamedContributor.of(entry.getKey(), entry.getValue()))
            .iterator();
    }

    /**
     * Gera relatório completo de saúde.
     * 
     * @return Mapa com status de todos os contribuidores
     */
    public Map<String, String> generateHealthReport() {
        Map<String, String> report = new HashMap<>();
        contributors.forEach((name, contributor) -> {
            if (contributor instanceof HealthIndicator) {
                report.put(name, ((HealthIndicator) contributor).health().getStatus().getCode());
            }
        });
        return report;
    }
}
