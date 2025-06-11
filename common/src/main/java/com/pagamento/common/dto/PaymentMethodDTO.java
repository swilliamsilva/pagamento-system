package com.pagamento.common.dto;

import com.pagamento.common.model.PaymentMethod.Type;
import java.time.LocalDate;

public class PaymentMethodDTO {
    private String id;
    private String userId;
    private Type type;
    private String bankName; // Itaú, Caixa, Santander, PagBank, etc.
    
    // Detalhes específicos por tipo de pagamento
    private CardDetailsDTO cardDetails;
    private PixDetailsDTO pixDetails;
    private BoletoDetailsDTO boletoDetails;
    private PlatformDetailsDTO platformDetails; // Para gateways como PagBank

    // Getters e Setters (padrão) 
    // ...

    // --- Classes Internas para Detalhes ---
    public static class CardDetailsDTO {
        private String cardNumber;
        private String cardHolder;
        private String expiryDate;
        private String cvv;
        private String brand; // Visa, Mastercard, etc.
        // Getters/Setters
    }

    public static class PixDetailsDTO {
        private String key; // CPF, CNPJ, email, chave aleatória
        private String beneficiary;
        private String bank; // Banco emissor do PIX
        // Getters/Setters
    }

    public static class BoletoDetailsDTO {
        private String codigoBarras;
        private LocalDate dataVencimento;
        private String beneficiario;
        // Getters/Setters
    }

    public static class PlatformDetailsDTO {
        private String platformId; // ID na plataforma (ex: PagBank)
        private String gatewayToken; // Token de segurança
        // Getters/Setters
    }
}