/* ========================================================
# Classe: LoggingAspect
# Módulo: pagamento-common-observability
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Website: simuleagora.com
# ======================================================== */

package com.pagamento.common.observability;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Span;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Aspect para logging centralizado e tracing distribuído
 * 
 * <p>Responsável por interceptar chamadas de métodos e registrar:
 * <ul>
 *   <li>Tempo de execução</li>
 *   <li>Argumentos (com filtro para dados sensíveis)</li>
 *   <li>Erros</li>
 *   <li>Contexto de tracing distribuído</li>
 * </ul>
 * 
 * <h2>Fluxo Principal</h2>
 * <ol>
 *   <li><b>Entrada:</b> Intercepta chamadas a métodos em com.pagamento.*</li>
 *   <li><b>Processamento:</b>
 *     <ul>
 *       <li>Configura contexto de tracing (traceId, spanId)</li>
 *       <li>Registra entrada com argumentos (filtrados)</li>
 *       <li>Executa método original</li>
 *       <li>Mede tempo de execução</li>
 *     </ul>
 *   </li>
 *   <li><b>Saída:</b>
 *     <ul>
 *       <li>Registra sucesso com tempo de execução</li>
 *       <li>Ou registra falha com mensagem de erro</li>
 *       <li>Limpa contexto MDC</li>
 *     </ul>
 *   </li>
 * </ol>
 * 
 * <h2>Relação com Outras Classes</h2>
 * <table>
 *   <tr><th>Classe</th><th>Relação</th></tr>
 *   <tr><td>Tracer</td><td>Fornece contexto de tracing distribuído</td></tr>
 *   <tr><td>MDC</td><td>Armazena contexto para logging estruturado</td></tr>
 *   <tr><td>ProceedingJoinPoint</td><td>Representa o método interceptado</td></tr>
 * </table>
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final Tracer tracer;

    public LoggingAspect(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Intercepta métodos para logging e tracing
     * 
     * @param joinPoint Ponto de junção com informações do método interceptado
     * @return Retorno do método original
     * @throws Throwable Exceção lançada pelo método original
     */
    @Around("execution(* com.pagamento..*.*(..)) && " +
            "!execution(* com.pagamento.common.observability..*.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        
        // Filtrar argumentos sensíveis para logging
        String args = Arrays.stream(joinPoint.getArgs())
                .map(this::safeArgToString)
                .collect(Collectors.joining(", "));
        
        // Configurar contexto de tracing
        String traceId = "no-trace";
        String spanId = "no-span";
        
        Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            traceId = currentSpan.context().traceId();
            spanId = currentSpan.context().spanId();
        }
        
        MDC.put("traceId", traceId);
        MDC.put("spanId", spanId);
        MDC.put("logId", UUID.randomUUID().toString().substring(0, 8));
        
        logger.debug("Method {} called with args: [{}]", methodName, args);
        
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            logger.debug("Method {} executed in {} ms", methodName, duration);
            return result;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Method {} failed after {} ms: {}",
                    methodName, duration, ex.getMessage());
            throw ex;
        } finally {
            MDC.clear();
        }
    }
    
    /**
     * Converte argumentos para string com proteção de dados sensíveis
     * 
     * @param arg Argumento a ser convertido
     * @return Representação segura do argumento para logging
     */
    private String safeArgToString(Object arg) {
        if (arg == null) return "null";
        
        // Ocultar dados sensíveis
        String className = arg.getClass().getSimpleName().toLowerCase();
        if (className.contains("card") || className.contains("senha") || 
            className.contains("password") || className.contains("token")) {
            return "[PROTECTED]";
        }
        
        // Limitar tamanho de strings longas
        String str = arg.toString();
        return str.length() > 100 ? str.substring(0, 100) + "..." : str;
    }
}


/* Funcionamento Detalhado:

    Interceptação:

        Captura todas as chamadas a métodos no pacote com.pagamento (exceto observability)

    Pré-processamento:

        Coleta argumentos (com filtro de dados sensíveis)

        Configura tracing distribuído (traceId, spanId)

        Gera log de entrada

    Execução:

        Chama o método original

        Mede tempo de execução

    Pós-processamento:

        Em caso de sucesso: log com tempo de execução

        Em caso de erro: log de falha com mensagem

        Limpeza do contexto MDC*/