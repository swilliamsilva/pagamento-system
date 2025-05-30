package com.pagamento.boleto.controller;

import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.model.BoletoResponse;
import com.pagamento.boleto.service.BoletoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boletos")
public class BoletoController {

    @Autowired
    private BoletoService boletoService;

    @PostMapping("/pagar")
    public ResponseEntity<BoletoResponse> pagarBoleto(@RequestBody Boleto boleto) {
        BoletoResponse response = boletoService.processarBoleto(boleto);
        return ResponseEntity.ok(response);
    }
}
