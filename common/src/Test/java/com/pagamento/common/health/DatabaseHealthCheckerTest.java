package com.pagamento.common.health;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DatabaseHealthCheckerTest {

    private JdbcTemplate jdbcTemplate;
    private DatabaseHealthChecker healthChecker;

    @Before
    public void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        healthChecker = new DatabaseHealthChecker(jdbcTemplate);
    }

    @Test
    public void testCheckHealthShouldReturnUpWhenDatabaseIsAvailable() {
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);

        Health health = healthChecker.checkHealth();

        assertNotNull(health);
        assertEquals("UP", health.getStatus().getCode());
        assertEquals(1, health.getDetails().get("result"));
        assertTrue(health.getDetails().containsKey("response_time_ms"));
    }

    @Test
    public void testCheckHealthShouldReturnDownWhenDatabaseThrowsException() {
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new RuntimeException("Connection failed"));

        Health health = healthChecker.checkHealth();

        assertNotNull(health);
        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals("Connection failed", health.getDetails().get("error"));
        assertTrue(health.getDetails().containsKey("response_time_ms"));
    }
}
