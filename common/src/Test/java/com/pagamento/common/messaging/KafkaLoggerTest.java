/* ========================================================
# Classe: KafkaLoggerTest
# Módulo: pagamento-common-messaging
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Website: simuleagora.com
# ======================================================== */

package com.pagamento.common.messaging;

import com.pagamento.common.dto.LogMessageDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KafkaLoggerTest {

    private KafkaTemplateWrapper wrapperMock;
    private KafkaLogger kafkaLogger;

    @Before
    public void setup() {
        wrapperMock = mock(KafkaTemplateWrapper.class);
        kafkaLogger = new KafkaLogger(wrapperMock, "topic-logs");
    }

    @Test
    public void deveEnviarMensagemDeLogComTodosOsCampos() {
        String nivel = "INFO";
        String mensagem = "Teste de log";
        String classe = "MinhaClasse";
        String metodo = "meuMetodo";

        kafkaLogger.log(nivel, mensagem, classe, metodo);

        ArgumentCaptor<LogMessageDTO> captor = ArgumentCaptor.forClass(LogMessageDTO.class);
        verify(wrapperMock).send(eq("topic-logs"), captor.capture());

        LogMessageDTO enviado = captor.getValue();
        assertEquals(nivel, enviado.getLevel());
        assertEquals(mensagem, enviado.getMensagem());
        assertEquals(classe, enviado.getClasse());
        assertEquals(metodo, enviado.getMetodo());
        assertNotNull(enviado.getTimestamp());
    }

    @Test
    public void naoDeveLancarExcecaoQuandoWrapperForNulo() {
        KafkaLogger semWrapper = new KafkaLogger(null, "topico-vazio");
        try {
            semWrapper.log("ERROR", "Falha crítica", "ClasseX", "metodoY");
        } catch (Exception e) {
            fail("Não deveria lançar exceção com wrapper nulo");
        }
    }
}
