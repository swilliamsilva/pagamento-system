/* ========================================================
# Classe: DefaultMeterObservationHandler
# Módulo: pagamento-common-observability
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Website: simuleagora.com
# ======================================================== */

package com.pagamento.common.observability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * @Component para manipulação de observações do Micrometer
 * 
 * <p>Implementa um ObservationHandler padrão para registrar métricas
 * de execução de operações observáveis no sistema.</p>
 * 
 * <h2>Fluxo Principal</h2>
 * <ol>
 *   <li><b>Entrada:</b> Recebe eventos de observação do Micrometer</li>
 *   <li><b>Processamento:</b>
 *     <ul>
 *       <li>OnStart: Registra início da observação</li>
 *       <li>OnError: Registra falhas durante a observação</li>
 *       <li>OnEvent: Captura eventos customizados</li>
 *       <li>OnStop: Finaliza a observação e registra métricas</li>
 *     </ul>
 *   </li>
 *   <li><b>Saída:</b> Métricas registradas no MeterRegistry</li>
 * </ol>
 * 
 * <h2>Relação com Outras Classes</h2>
 * <table>
 *   <tr><th>Classe</th><th>Relação</th></tr>
 *   <tr><td>Observation</td><td>Fornece o contexto de observação</td></tr>
 *   <tr><td>MeterRegistry</td><td>Armazena as métricas coletadas</td></tr>
 *   <tr><td>ObservationHandler</td><td>Interface base para manipulação</td></tr>
 * </table>
 * 
 * @see <a href="https://micrometer.io/docs/observation">Documentação Micrometer Observation</a>
 */
@Component
public class DefaultMeterObservationHandler implements ObservationHandler<Observation.Context> {

    private final MeterRegistry meterRegistry;

    public DefaultMeterObservationHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void onStart(Observation.Context context) {
        meterRegistry.counter("observation.start", 
            "name", context.getName(),
            "context", context.getClass().getSimpleName())
        .increment();
    }

    @Override
    public void onError(Observation.Context context) {
        meterRegistry.counter("observation.error",
            "name", context.getName(),
            "error", context.getError().getClass().getSimpleName())
        .increment();
    }

    @Override
    public void onEvent(Observation.Event event, Observation.Context context) {
        meterRegistry.counter("observation.event",
            "name", context.getName(),
            "event", event.getName())
        .increment();
    }

    @Override
    public void onStop(Observation.Context context) {
        meterRegistry.timer("observation.duration",
            "name", context.getName())
        .record(context.getDuration());
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }
}

/*Funcionamento Detalhado:

    Registro de Métricas:

        onStart: Contador de inícios de observação

        onError: Contador de erros durante observações

        onEvent: Contador de eventos customizados

        onStop: Timer para duração da observação

    Tags e Dimensões:

        Todas as métricas incluem tags para filtragem e análise

        Nome da observação sempre incluído como tag

    Integração:

        Automaticamente registrado pelo Spring Boot

        Processa todas as observações do aplicativo**/