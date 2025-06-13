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
import org.springframework.context.annotation.Lazy;
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

    public LoggingAspect(@Lazy Tracer tracer) {
        this.tracer = tracer;
    }

    @Around("execution(* com.pagamento..*.*(..)) && " +
            "!execution(* com.pagamento.common.observability..*.*(..))")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();

        String args = Arrays.stream(joinPoint.getArgs())
                .map(this::safeArgToString)
                .collect(Collectors.joining(", "));

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
            logger.error("Method {} failed after {} ms: {}", methodName, duration, ex.getMessage());
            throw ex;
        } finally {
            MDC.clear();
        }
    }

    private String safeArgToString(Object arg) {
        if (arg == null) return "null";

        String className = arg.getClass().getSimpleName().toLowerCase();
        if (className.contains("card") || className.contains("senha") ||
            className.contains("password") || className.contains("token")) {
            return "[PROTECTED]";
        }

        String str = arg.toString();
        return str.length() > 100 ? str.substring(0, 100) + "..." : str;
    }
}
