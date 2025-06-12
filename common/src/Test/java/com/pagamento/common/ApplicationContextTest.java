//common\src\test\java\com\pagamento\common\ApplicationContextTest.java
package com.pagamento.common.health;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.pagamento.common.PagamentoSystemApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PagamentoSystemApplication.class)
@Tag(name = "Application Context", description = "Testes de configuração do contexto Spring")
public class ApplicationContextTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @Operation(summary = "Verificar carga do contexto Spring",
              description = "Testa se o contexto Spring é carregado corretamente e valida beans essenciais",
              responses = {
                  @ApiResponse(responseCode = "200", description = "Contexto carregado com sucesso"),
                  @ApiResponse(responseCode = "500", description = "Falha na configuração do Spring")
              })
    public void shouldLoadApplicationContext() {
        assertNotNull(applicationContext, "O contexto da aplicação não deve ser nulo");
        assertNotNull(applicationContext.getBean("nomeDoBeanPrincipal"), 
                    "Bean principal não encontrado no contexto");
    }

    @Test
    @Operation(summary = "Verificar conexão com banco de dados",
              description = "Testa se a configuração do datasource está correta")
    public void shouldHaveDatabaseConnection() {
        assertNotNull(applicationContext.getBean("dataSource"), 
                    "DataSource não configurado corretamente");
    }
}
