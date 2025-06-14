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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Tag(
    name = "Health Registry",
    description = "Registro e gerenciamento centralizado de verificadores de saúde"
)
public class HealthContributorRegistry implements HealthContributor, Iterable<NamedContributor<HealthContributor>> {

    private final Map<String, HealthContributor> contributors = new HashMap<>();

    public void registerContributor(String name, HealthContributor contributor) {
        contributors.put(name, contributor);
    }

    public HealthContributor getContributor(String name) {
        return contributors.get(name);
    }

    @Override
    public Iterator<NamedContributor<HealthContributor>> iterator() {
        return contributors.entrySet().stream()
            .map(entry -> NamedContributor.of(entry.getKey(), entry.getValue()))
            .iterator();
    }

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
