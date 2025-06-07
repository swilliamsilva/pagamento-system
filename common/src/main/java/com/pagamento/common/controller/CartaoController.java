package com.pagamento.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagamento.common.dto.CartaoDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @PostMapping
    public ResponseEntity criarCartao(@Valid @RequestBody CartaoDTO cartaoDTO) {
        // Processamento
        return ResponseEntity.ok(cartaoDTO);
    }
    
}