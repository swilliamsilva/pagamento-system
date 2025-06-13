package com.pagamento.common.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
    "spring.main.allow-circular-references=true",
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
public class ApplicationContextTest {

    @Autowired(required = false)
    private ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        assertNotNull(applicationContext, "O contexto Spring deve ser carregado corretamente");
    }

    @Test
    public void shouldHaveDataSourceConfigured() {
        if (applicationContext != null) {
            DataSource dataSource = applicationContext.getBeanProvider(DataSource.class).getIfAvailable();
            assertNotNull(dataSource, "DataSource deve estar configurado no contexto");
        }
    }
}