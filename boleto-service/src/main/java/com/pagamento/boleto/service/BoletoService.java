package com.pagamento.boleto.service;

import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.dto.BoletoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class BoletoService {

    public BoletoResponse processarBoleto(Boleto boleto) {
        // Validar boleto
        if (!validarBoleto(boleto)) {
            return criarResposta(boleto, null, "REJEITADO", "Boleto inválido ou vencido");
        }

        // Simular processamento bancário
        String numeroControle = gerarNumeroControle();
        
        // Simular falha aleatória (5% de chance)
        if (simularFalhaProcessamento()) {
            return criarResposta(boleto, numeroControle, "FALHA", "Falha no processamento do banco");
        }

        // Pagamento bem-sucedido
        return criarResposta(boleto, numeroControle, "PAGO", "Pagamento realizado com sucesso");
    }

    private BoletoResponse criarResposta(Boleto boleto, String numeroControle, String status, String mensagem) {
        return new BoletoResponse(
            boleto.getCodigoBarras(),
            numeroControle,
            LocalDateTime.now(),
            status,
            mensagem
        );
    }

    private boolean validarBoleto(Boleto boleto) {
        // Validações básicas já feitas pela anotação @Valid
        // Verificação adicional de vencimento
        return !boleto.getDataVencimento().isBefore(LocalDate.now());
    }

    private String gerarNumeroControle() {
        return "CTRL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean simularFalhaProcessamento() {
        return new Random().nextDouble() < 0.05;
    }
}