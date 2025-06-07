package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;

/**
 * LivenessProbe indica se o aplicativo está vivo e funcionando.
 * Verifica o estado interno da JVM e recursos críticos.
 * 
 * @apiNote Health Check para Kubernetes
 * @endpoint /actuator/health/liveness
 */
@Component("livenessProbe")
public class LivenessProbe implements HealthIndicator {

    private static final double MAX_HEAP_USAGE_PERCENT = 0.95; // 95% de uso máximo de heap
    private static final int MAX_THREADS = 500; // Limite de threads

    @Override
    public Health health() {
        try {
            Health.Builder builder = (Builder) Health.up();
            
            // Verifica uso de memória
            MemoryUsage heapUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            double usedPercent = (double) heapUsage.getUsed() / heapUsage.getMax();
            builder.withDetail("heap_used", heapUsage.getUsed())
            /*Cannot invoke withDetail(String, long) on the primitive type void */
                   .withDetail("heap_max", heapUsage.getMax())
                   .withDetail("heap_percent", String.format("%.2f%%", usedPercent * 100));
            
            if (usedPercent > MAX_HEAP_USAGE_PERCENT) {
                ((Builder) builder.down()).withDetail("error", "High heap memory usage");
            }
            
            // Verifica contagem de threads
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            int threadCount = threadBean.getThreadCount();
            builder.withDetail("thread_count", threadCount);
            
            if (threadCount > MAX_THREADS) {
                ((Builder) builder.down()).withDetail("error", "High thread count");
            }
            
            // Verificação de deadlocks
            long[] deadlockedThreads = threadBean.findDeadlockedThreads();
            if (deadlockedThreads != null && deadlockedThreads.length > 0) {
                builder.down().withDetail("error", "Deadlock detected");
                /* The method withDetail(String, String) is undefined for the type Object*/
            }
            
            return builder.build();
                
        } catch (Exception e) {
            return Health.down()
            		/*Cannot invoke build() on the primitive type void */
                .withDetail("reason", "Exception in health check: " + e.getMessage())
                .build();
        }
    }
}
