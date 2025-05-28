package com.pagamento.pagbank.client;

import com.pagamento.pagbank.dto.PaymentRequest;
import com.pagamento.pagbank.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PagBankFallback implements PagBankClient {

    @Override
    public PaymentResponse sendPayment(PaymentRequest request) {
        PaymentResponse fallback = new PaymentResponse();
        fallback.setStatus("FALLBACK");
        fallback.setMessage("Servico PagBank indisponivel. Pagamento nao realizado.");
        return fallback;
    }

    @Override
    public PaymentResponse checkStatus(String transactionId) {
        PaymentResponse fallback = new PaymentResponse();
        fallback.setStatus("FALLBACK");
        fallback.setMessage("Servico PagBank indisponivel. Status nao verificado.");
        return fallback;
    }

    @Override
    public PaymentResponse refundPayment(PaymentRequest request) {
        PaymentResponse fallback = new PaymentResponse();
        fallback.setStatus("FALLBACK");
        fallback.setMessage("Servico PagBank indisponivel. Reembolso nao efetuado.");
        return fallback;
    }
}
