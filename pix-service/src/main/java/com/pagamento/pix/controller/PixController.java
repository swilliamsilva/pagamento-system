package com.pagamento.pix.controller;

import com.pagamento.pix.model.PixRequest;
import com.pagamento.pix.model.PixResponse;
import com.pagamento.pix.service.PixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pix")
public class PixController {

    @Autowired
    private PixService pixService;

    @PostMapping("/pagar")
    public ResponseEntity<PixResponse> processarPix(@RequestBody PixRequest pixRequest) {
        PixResponse response = pixService.processarPix(pixRequest);
        return ResponseEntity.ok(response);
    }
}
