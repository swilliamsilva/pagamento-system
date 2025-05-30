package com.pagamento.pix.service;

import com.pagamento.pix.model.PixRequest;
import com.pagamento.pix.model.PixResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class PixService {

    public PixResponse processarPix(PixRequest pixRequest) {
        // Validar chave PIX
        if (!validarChavePix(pixRequest.getChave())) {
            return new PixResponse(
                null,
                pixRequest.getChave(),
                0,
                LocalDateTime.now(),
                "REJEITADO",
                null,
                "Chave PIX inválida"
            );
        }

        // Gerar ID de transação
        UUID idTransacao = UUID.randomUUID();
        
        // Gerar QR Code
        String qrCode = gerarQrCode(pixRequest);
        
        // Simular falha aleatória (3% de chance)
        if (simularFalhaProcessamento()) {
            return new PixResponse(
                idTransacao,
                pixRequest.getChave(),
                pixRequest.getValor().doubleValue(),
                LocalDateTime.now(),
                "FALHA",
                qrCode,
                "Falha no processamento PIX"
            );
        }

        return new PixResponse(
            idTransacao,
            pixRequest.getChave(),
            pixRequest.getValor().doubleValue(),
            LocalDateTime.now(),
            "CONCLUIDO",
            qrCode,
            "Pagamento PIX realizado com sucesso"
        );
    }

    private boolean validarChavePix(String chave) {
        // Validação simplificada (deveria usar regex para cada tipo)
        if (chave == null || chave.trim().isEmpty()) return false;
        
        // CPF (11 dígitos)
        if (chave.matches("^\d{11}$")) return true;
        
        // Email
        if (chave.contains("@") && chave.contains(".")) return true;
        
        // Telefone (com DDD)
        if (chave.matches("^\+?\d{10,13}$")) return true;
        
        // Chave aleatória (UUID)
        if (chave.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")) return true;
        
        return false;
    }

    private String gerarQrCode(PixRequest pixRequest) {
        // Gerar representação simplificada de QR Code
        return "PIX|" + pixRequest.getChave() + "|" + 
               pixRequest.getValor() + "|" + 
               pixRequest.getBeneficiario() + "|" +
               UUID.randomUUID().toString().substring(0, 8);
    }

    private boolean simularFalhaProcessamento() {
        return new Random().nextDouble() < 0.03;
    }
}
