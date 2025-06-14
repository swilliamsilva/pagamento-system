/* ========================================================
# Classe de Teste: DefaultHealthEndpointGroupsTest
# Módulo: pagamento-common-health
# Autor: William Silva
# Tecnologias: Java 8, Spring Boot 2.7, JUnit 4, Mockito
# Descrição: Testes unitários para a classe DefaultHealthEndpointGroups
# ======================================================== */

package com.pagamento.common.health;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.HealthEndpointGroup;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultHealthEndpointGroupsTest {

    private DefaultHealthEndpointGroups groups;
    private HealthEndpointGroup defaultGroup;
    private HealthEndpointGroup dbGroup;
    private HealthEndpointGroup kafkaGroup;

    @Before
    public void setUp() {
        defaultGroup = mock(HealthEndpointGroup.class);
        dbGroup = mock(HealthEndpointGroup.class);
        kafkaGroup = mock(HealthEndpointGroup.class);

        Map<String, HealthEndpointGroup> namedGroups = new HashMap<>();
        namedGroups.put("db", dbGroup);
        namedGroups.put("kafka", kafkaGroup);

        groups = new DefaultHealthEndpointGroups(namedGroups, defaultGroup);
    }

    @Test
    public void testGetPrimaryReturnsDefaultGroup() {
        assertSame(defaultGroup, groups.getPrimary());
    }

    @Test
    public void testGetReturnsNamedGroup() {
        assertSame(dbGroup, groups.get("db"));
        assertSame(kafkaGroup, groups.get("kafka"));
    }

    @Test
    public void testGetReturnsDefaultGroupWhenNotFound() {
        assertSame(defaultGroup, groups.get("nonexistent"));
    }

    @Test
    public void testGetNamesReturnsCorrectSet() {
        Set<String> names = groups.getNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("db"));
        assertTrue(names.contains("kafka"));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullMapThrowsException() {
        new DefaultHealthEndpointGroups(null, defaultGroup);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullDefaultGroupThrowsException() {
        new DefaultHealthEndpointGroups(new HashMap<>(), null);
    }
}
