package api.dto;

import com.pagamento.common.validation.ValidCPF;

public class ClienteDTO {

    @ValidCPF
    private String cpf;

    // ...
}
