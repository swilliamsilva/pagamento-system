package com.pagamento.boleto.service;

import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.model.BoletoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class BoletoService {

    public BoletoResponse processarBoleto(Boleto boleto) {
        // Validar boleto
        if (!validarBoleto(boleto)) {
            return new BoletoResponse(
                boleto.getCodigoBarras(),
                null,
                LocalDateTime.now(),
                "REJEITADO",
                "Boleto inválido ou vencido"
            );
        }

        // Simular processamento bancário
        String numeroControle = gerarNumeroControle();
        
        // Simular falha aleatória (5% de chance)
        if (simularFalhaProcessamento()) {
            return new BoletoResponse(
                boleto.getCodigoBarras(),
                numeroControle,
                LocalDateTime.now(),
                "FALHA",
                "Falha no processamento do banco"
            );
        }

        // Pagamento bem-sucedido
        return new BoletoResponse(
            boleto.getCodigoBarras(),
            numeroControle,
            LocalDateTime.now(),
            "PAGO",
            "Pagamento realizado com sucesso"
        );
    }

    private boolean validarBoleto(Boleto boleto) {
        // Verificar se o código de barras é válido (simulação)
        if (boleto.getCodigoBarras() == null || boleto.getCodigoBarras().length() != 44) {
            return false;
        }
        
        // Verificar se o boleto está vencido
        if (boleto.getDataVencimento().isBefore(LocalDate.now())) {
            return false;
        }
        
        // Verificar valor positivo
        return boleto.getValor().compareTo(java.math.BigDecimal.ZERO) > 0;
    }

    private String gerarNumeroControle() {
        Random rand = new Random();
        return "CTRL" + String.format("%010d", rand.nextInt(1000000000));
    }

    private boolean simularFalhaProcessamento() {
        return new Random().nextDouble() < 0.05;
    }
}
