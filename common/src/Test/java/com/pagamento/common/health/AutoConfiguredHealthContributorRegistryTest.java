/* ========================================================
# Classe de Teste: AutoConfiguredHealthContributorRegistryTest
# Módulo: pagamento-common-health
# Autor: William Silva
# Tecnologias: Java 8, Spring Boot 2.7, JUnit 4, Mockito
# Descrição: Testes para o registry customizado de HealthContributors
# ======================================================== */

package com.pagamento.common.health;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.*;

import static org.junit.Assert.*;

public class AutoConfiguredHealthContributorRegistryTest {

    private AutoConfiguredHealthContributorRegistry registry;

    private static class DummyHealthIndicator implements HealthIndicator {
        private final String name;

        DummyHealthIndicator(String name) {
            this.name = name;
        }

        @Override
        public Health health() {
            return Health.up().withDetail("name", name).build();
        }
    }

    @Before
    public void setUp() {
        Map<String, HealthContributor> initial = new HashMap<>();
        initial.put("db", new DummyHealthIndicator("db"));
        registry = new AutoConfiguredHealthContributorRegistry(initial);
    }

    @Test
    public void testGetContributor() {
        HealthContributor contributor = registry.getContributor("db");
        assertNotNull(contributor);
        assertTrue(contributor instanceof HealthIndicator);
    }

    @Test
    public void testRegisterContributor() {
        registry.registerContributor("kafka", new DummyHealthIndicator("kafka"));
        assertNotNull(registry.getContributor("kafka"));
    }

    @Test
    public void testUnregisterContributor() {
        HealthContributor removed = registry.unregisterContributor("db");
        assertNotNull(removed);
        assertNull(registry.getContributor("db"));
    }

    @Test
    public void testIterator() {
        List<String> names = new ArrayList<>();
        registry.iterator().forEachRemaining(named -> names.add(named.getName()));
        assertTrue(names.contains("db"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNullContributorThrowsException() {
        registry.registerContributor("null", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnregisterNullNameThrowsException() {
        registry.unregisterContributor(null);
    }
}
