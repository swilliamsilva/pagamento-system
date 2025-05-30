package com.pagamento.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment.gateway")
public class GatewayConfig {
    private String defaultProvider;
    private double feePercentage;
    private int timeoutSeconds;

    // Getters e Setters
    public String getDefaultProvider() { return defaultProvider; }
    public void setDefaultProvider(String defaultProvider) { this.defaultProvider = defaultProvider; }
    public double getFeePercentage() { return feePercentage; }
    public void setFeePercentage(double feePercentage) { this.feePercentage = feePercentage; }
    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
}
