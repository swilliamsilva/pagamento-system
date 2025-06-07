package com.pagamento.api.controller;

import com.pagamento.api.dto.PagamentoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @PostMapping
    public ResponseEntity<String> criarPagamento(@RequestBody @Valid PagamentoDTO pagamento) {
        return ResponseEntity.ok("Pagamento criado com valor: R$ " + pagamento.getValor());
    }
}
