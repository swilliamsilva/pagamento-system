package com.pagamento.boleto.service;

import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.model.BoletoResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class BoletoService {

    private final Random random;

    public BoletoService() {
        this.random = new Random();
    }

    public BoletoService(Random random) {
        this.random = random;
    }

    public BoletoResponse processarBoleto(Boleto boleto) {
        if (!validarBoleto(boleto)) {
            return criarResposta(boleto, null, "REJEITADO", "Boleto inválido ou vencido");
        }

        String numeroControle = gerarNumeroControle();

        if (simularFalhaProcessamento()) {
            return criarResposta(boleto, numeroControle, "FALHA", "Falha no processamento do banco");
        }

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
        return boleto.getCodigoBarras() != null &&
               boleto.getCodigoBarras().length() == 44 &&
               boleto.getDataVencimento() != null &&
               !boleto.getDataVencimento().isBefore(LocalDate.now()) &&
               boleto.getValor() != null &&
               boleto.getValor().compareTo(BigDecimal.ZERO) > 0;
    }

    private String gerarNumeroControle() {
        return "CTRL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean simularFalhaProcessamento() {
        return random.nextDouble() < 0.05;
    }
}
