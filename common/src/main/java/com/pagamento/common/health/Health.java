package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health.Builder;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import org.springframework.boot.actuate.health.HealthEndpointGroups;

/**
 * ========================================================
 * CLASSE HEALTH - REPRESENTAÇÃO DO STATUS DE SAÚDE
 * 
 * @tag Health Status
 * @summary Modela o status de saúde de componentes do sistema
 * @author William Silva
 * @contact williamsilva.codigo@gmail.com
 * @website simuleagora.com
 * ========================================================
 */
public class Health {
    
    public static Builder up() {
        return new Builder().up();
    }
    
    public static Builder down() {
        return new Builder().down();
    }
    
    public static Builder status(String status) {
        return new Builder().status(status);
    }
    
    // Implementação interna do Builder
    public static class Builder {
        private String status;
        private Map<String, Object> details = LinkedHashMap.empty();
        
        public Builder up() {
            this.status = "UP";
            return this;
        }
        
        public Builder down() {
            this.status = "DOWN";
            return this;
        }
        
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        
        public Builder withDetail(String key, Object value) {
            this.details = this.details.put(key, value);
            return this;
        }
        
        public Health build() {
            return new Health(this.status, this.details);
        }
    }
    
    // Campos e construtor real da classe Health
    private final String status;
    private final Map<String, Object> details;
    
    private Health(String status, Map<String, Object> details) {
        this.status = status;
        this.details = details; // Vavr Maps are immutable by default
    }
    
    // Getters
    public String getStatus() {
        return this.status;
    }
    
    public Map<String, Object> getDetails() {
        return this.details;
    }
}