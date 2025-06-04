package com.pagamento.boleto.controller;

import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.dto.BoletoResponse;
import com.pagamento.boleto.service.BoletoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/boletos")
public class BoletoController {

    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @PostMapping("/pagar")
    public ResponseEntity<BoletoResponse> pagarBoleto(@Valid @RequestBody Boleto boleto) {
        BoletoResponse response = boletoService.processarBoleto(boleto);
        return ResponseEntity.ok(response);
    }
}