package com.pagamento.common.observability;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import io.micrometer.tracing.Tracer;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final Tracer tracer;

    public LoggingAspect(Tracer tracer) {
        this.tracer = tracer;
    }

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
        
        if (tracer.currentSpan() != null) {
            traceId = tracer.currentSpan().context().traceId();
            spanId = tracer.currentSpan().context().spanId();
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
