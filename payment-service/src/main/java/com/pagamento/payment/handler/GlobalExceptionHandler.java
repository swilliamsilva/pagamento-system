package com.pagamento.payment.handler;

import com.pagamento.payment.dto.PaymentResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PaymentResponseDTO> handleException(Exception e) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setStatus("ERROR");
        response.setMessage("Payment processing failed: " + e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
