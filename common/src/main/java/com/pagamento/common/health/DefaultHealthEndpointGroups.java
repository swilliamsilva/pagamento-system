/* ========================================================
# Classe: DefaultHealthEndpointGroup
# Módulo: pagamento-common-health
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Website: simuleagora.com
# Tecnologias: Java 8, Spring Boot 2.7, Maven - Junho 2025
# ======================================================== */

package com.pagamento.common.health;

import org.springframework.boot.actuate.health.HealthEndpointGroup;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.health.StatusAggregator;
import org.springframework.boot.actuate.health.HttpCodeStatusMapper;
import org.springframework.boot.actuate.health.AdditionalHealthEndpointPath;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementação customizada de grupos de endpoints de saúde
 */
@Component
public class DefaultHealthEndpointGroups implements HealthEndpointGroups, Map<String, HealthEndpointGroup> {

    private final Map<String, HealthEndpointGroup> groups = new ConcurrentHashMap<>();
    private final HealthEndpointGroup defaultGroup;

    public DefaultHealthEndpointGroups() {
        this.defaultGroup = new SimpleHealthEndpointGroup(
            true, 
            ShowDetails.ALWAYS, 
            Collections.emptySet(),
            StatusAggregator.getDefault(),
            HttpCodeStatusMapper.DEFAULT,
            null
        );
    }

    public DefaultHealthEndpointGroups(Map<String, HealthEndpointGroup> groups, 
                                     HealthEndpointGroup defaultGroup) {
        this.groups.putAll(groups);
        this.defaultGroup = defaultGroup;
    }

    @Override
    public HealthEndpointGroup getPrimary() {
        return defaultGroup;
    }

    @Override
    public HealthEndpointGroup get(String name) {
        return groups.getOrDefault(name, defaultGroup);
    }

    @Override
    public Set<String> getNames() {
        return Collections.unmodifiableSet(groups.keySet());
    }

    // Implementações do Map interface
    @Override
    public int size() {
        return groups.size();
    }

    @Override
    public boolean isEmpty() {
        return groups.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return groups.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return groups.containsValue(value);
    }

    @Override
    public HealthEndpointGroup get(Object key) {
        return groups.get(key);
    }

    @Override
    public HealthEndpointGroup put(String key, HealthEndpointGroup value) {
        return groups.put(key, value);
    }

    @Override
    public HealthEndpointGroup remove(Object key) {
        return groups.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends HealthEndpointGroup> m) {
        groups.putAll(m);
    }

    @Override
    public void clear() {
        groups.clear();
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(groups.keySet());
    }

    @Override
    public Collection<HealthEndpointGroup> values() {
        return Collections.unmodifiableCollection(groups.values());
    }

    @Override
    public Set<Entry<String, HealthEndpointGroup>> entrySet() {
        return Collections.unmodifiableSet(groups.entrySet());
    }

    private static class SimpleHealthEndpointGroup implements HealthEndpointGroup {
        private final boolean includes;
        private final ShowDetails showDetails;
        private final Set<String> roles;
        private final StatusAggregator statusAggregator;
        private final HttpCodeStatusMapper httpCodeStatusMapper;
        private final AdditionalHealthEndpointPath additionalPath;

        public SimpleHealthEndpointGroup(boolean includes, 
                                       ShowDetails showDetails, 
                                       Set<String> roles,
                                       StatusAggregator statusAggregator,
                                       HttpCodeStatusMapper httpCodeStatusMapper,
                                       AdditionalHealthEndpointPath additionalPath) {
            this.includes = includes;
            this.showDetails = showDetails;
            this.roles = Collections.unmodifiableSet(new HashSet<>(roles));
            this.statusAggregator = statusAggregator;
            this.httpCodeStatusMapper = httpCodeStatusMapper;
            this.additionalPath = additionalPath;
        }

        @Override
        public boolean isMember(String name) {
            return includes;
        }

        public ShowDetails getShowDetails() {
            return showDetails;
        }

        public Set<String> getRoles() {
            return roles;
        }

        @Override
        public boolean showComponents(SecurityContext securityContext) {
            return false;
        }

        @Override
        public boolean showDetails(SecurityContext securityContext) {
            return showDetails != ShowDetails.NEVER;
        }

        @Override
        public StatusAggregator getStatusAggregator() {
            return statusAggregator;
        }

        @Override
        public HttpCodeStatusMapper getHttpCodeStatusMapper() {
            return httpCodeStatusMapper;
        }

        @Override
        public AdditionalHealthEndpointPath getAdditionalPath() {
            return additionalPath;
        }
    }
}