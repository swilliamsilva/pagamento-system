package com.pagamento.common.vault;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.vault.authentication.AppRoleAuthentication;
import org.springframework.vault.authentication.AppRoleAuthenticationOptions;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import java.net.URI;

@Configuration
public class VaultConfig {

    @Value("${vault.uri}")
    private String vaultUri;

    @Value("${vault.roleId}")
    private String roleId;

    @Value("${vault.secretId}")
    private String secretId;

    @Bean
    public VaultTemplate vaultTemplate() {
        VaultEndpoint endpoint = VaultEndpoint.from(URI.create(vaultUri));
        
        AppRoleAuthenticationOptions options = AppRoleAuthenticationOptions.builder()
            .roleId(AppRoleAuthenticationOptions.RoleId.provided(roleId))
            .secretId(AppRoleAuthenticationOptions.SecretId.provided(secretId))
            .build();
        
        ClientAuthentication authentication = new AppRoleAuthentication(options, endpoint);
        
        return new VaultTemplate(endpoint, authentication);
    }

    public String getSecret(String path) {
        VaultResponse response = vaultTemplate().read(path);
        return response != null ? response.getData().toString() : null;
    }
    
    public void storeSecret(String path, Object data) {
        vaultTemplate().write(path, data);
    }
}
