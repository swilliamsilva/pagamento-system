// HealthEndpointTest.java
package com.pagamento.common.health;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HealthEndpointTest {

    private HealthEndpoint endpoint;
    private HealthContributorRegistry registry;

    @Before
    public void setUp() {
        registry = mock(HealthContributorRegistry.class);
        endpoint = new HealthEndpoint(registry);
    }

    @Test
    public void testHealthAllUp() {
        HealthIndicator hi = mock(HealthIndicator.class);
        when(hi.health()).thenReturn(Health.up().build());
        when(registry.stream()).thenReturn(
            Collections.singletonMap("dummy", hi).entrySet().stream()
        );

        Health result = endpoint.health();
        assertEquals("UP", result.getStatus().getCode());
        Map<String,Object> details = result.getDetails();
        assertTrue(details.containsKey("dummy"));
    }

    @Test
    public void testHealthDownIfAnyDown() {
        HealthIndicator up = mock(HealthIndicator.class);
        HealthIndicator down = mock(HealthIndicator.class);
        when(up.health()).thenReturn(Health.up().build());
        when(down.health()).thenReturn(Health.down().withDetail("err","x").build());
        Map<String, HealthContributor> map = Collections.unmodifiableMap(
            Map.of("up", up, "down", down)
        );
        when(registry.stream()).thenReturn(map.entrySet().stream());

        Health result = endpoint.health();
        assertEquals("DOWN", result.getStatus().getCode());
        assertTrue(((Map<?,?>)result.getDetails()).containsKey("down"));
    }
}
