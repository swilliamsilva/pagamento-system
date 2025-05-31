package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health;

/**
 * Interface para verificadores de dependências usados pelo ReadinessProbe.
 * 
 * @apiNote Implemente esta interface para adicionar novas verificações de dependência
 */
public interface DependencyHealthChecker {
    /**
     * Verifica a saúde de uma dependência específica.
     * @return Objeto Health com detalhes do status
     */
    Health checkHealth();
}
