package com.pagamento.asaas;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class AsaasApplication {
    public static void main(String[] args) {
        SpringApplication.run(AsaasApplication.class, args);
    }
}
