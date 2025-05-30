package com.pagamento.card.service;

import com.pagamento.card.model.CardPaymentRequest;
import com.pagamento.card.model.CardPaymentResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Random;
import java.util.UUID;

@Service
public class CardService {

    public CardPaymentResponse processPayment(CardPaymentRequest request) {
        // Validação básica já feita via annotations, mas verificar expiração
        if (isCardExpired(request.getExpiryDate())) {
            return new CardPaymentResponse(
                null,
                "REJEITADO",
                "Cartão expirado",
                LocalDateTime.now(),
                null
            );
        }

        // Simular processamento com operadora de cartão
        UUID transactionId = UUID.randomUUID();
        String authCode = generateAuthorizationCode();
        
        // Simular falha aleatória (4% de chance)
        if (simulateProcessingFailure()) {
            return new CardPaymentResponse(
                transactionId,
                "FALHA",
                "Falha no processamento com a operadora",
                LocalDateTime.now(),
                null
            );
        }

        // Simular saldo insuficiente (10% de chance)
        if (simulateInsufficientFunds()) {
            return new CardPaymentResponse(
                transactionId,
                "REJEITADO",
                "Saldo insuficiente",
                LocalDateTime.now(),
                null
            );
        }

        // Pagamento bem-sucedido
        return new CardPaymentResponse(
            transactionId,
            "APROVADO",
            "Pagamento realizado com sucesso",
            LocalDateTime.now(),
            authCode
        );
    }

    private boolean isCardExpired(String expiryDate) {
        try {
            // Converter MM/YY para LocalDate
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);
            
            // Criar data de expiração (último dia do mês)
            LocalDateTime expiry = LocalDateTime.of(year, month, 1, 0, 0)
                .plusMonths(1)
                .minusDays(1);
            
            return expiry.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            return true; // Considera expirado se houver erro
        }
    }

    private String generateAuthorizationCode() {
        Random rand = new Random();
        return "AUTH" + String.format("%06d", rand.nextInt(1000000));
    }

    private boolean simulateProcessingFailure() {
        return new Random().nextDouble() < 0.04;
    }

    private boolean simulateInsufficientFunds() {
        return new Random().nextDouble() < 0.10;
    }
}
