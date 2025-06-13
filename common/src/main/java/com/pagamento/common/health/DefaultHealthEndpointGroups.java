package com.pagamento.common.health;

import org.springframework.boot.actuate.health.HealthEndpointGroup;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.boot.actuate.health.StatusAggregator;
import org.springframework.boot.actuate.health.HttpCodeStatusMapper;
import org.springframework.boot.actuate.health.AdditionalHealthEndpointPath;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultHealthEndpointGroups implements HealthEndpointGroups {
    
    private final Map<String, HealthEndpointGroup> groups = new ConcurrentHashMap<>();
    private final HealthEndpointGroup defaultGroup;
    
    public DefaultHealthEndpointGroups(Map<String, HealthEndpointGroup> groups, 
                                     HealthEndpointGroup defaultGroup) {
        this.groups.putAll(Objects.requireNonNull(groups));
        this.defaultGroup = Objects.requireNonNull(defaultGroup);
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

    private static class SimpleHealthEndpointGroup implements HealthEndpointGroup {
        private final boolean includes;
        private final String showDetails;
        private final Set<String> roles;
        private final StatusAggregator statusAggregator;
        private final HttpCodeStatusMapper httpCodeStatusMapper;
        private final AdditionalHealthEndpointPath additionalPath;

        public SimpleHealthEndpointGroup(boolean includes, 
                                      String showDetails,
                                      Set<String> roles,
                                      StatusAggregator statusAggregator,
                                      HttpCodeStatusMapper httpCodeStatusMapper,
                                      AdditionalHealthEndpointPath additionalPath) {
            this.includes = includes;
            this.showDetails = showDetails;
            this.roles = roles != null ? 
                Collections.unmodifiableSet(new HashSet<>(roles)) : 
                Collections.emptySet();
            this.statusAggregator = statusAggregator;
            this.httpCodeStatusMapper = httpCodeStatusMapper;
            this.additionalPath = additionalPath;
        }

        @Override
        public boolean isMember(String name) {
            return includes;
        }

        public String getShowDetails() {
            return showDetails;
        }

        public Set<String> getRoles() {
            return roles;
        }

        @Override
        public boolean showComponents(SecurityContext securityContext) {
            if ("ALWAYS".equals(showDetails)) {
                return true;
            }
            if (!"WHEN_AUTHORIZED".equals(showDetails)) {
                return false;
            }
            if (securityContext == null || securityContext.getPrincipal() == null) {
                return false;
            }
            return roles.isEmpty() || roles.stream()
                .anyMatch(r -> securityContext.isUserInRole(r));
        }

        @Override
        public boolean showDetails(SecurityContext securityContext) {
            return showComponents(securityContext);
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