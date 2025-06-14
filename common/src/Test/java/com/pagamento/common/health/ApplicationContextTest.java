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
    "spring.main.allow-circular-references=true"
    // Não desabilita mais o DataSource aqui, pois você quer testá-lo
	// @@@@@@Desabilitado apenas para avançar com os testes antes de veriicar o BD.	
})
public class ApplicationContextTest {

    @Autowired(required = false)
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private DataSource dataSource;

    @Test
    public void contextLoads() {
        assertNotNull(applicationContext, "O contexto Spring deve ser carregado corretamente");
    }

    @Test
    public void shouldHaveDataSourceConfigured() {
        // Agora valida direto a injeção
        assertNotNull(dataSource, "DataSource deve estar configurado no contexto");
    }
}
