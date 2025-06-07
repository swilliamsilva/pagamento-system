package com.pagamento.api.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pagamento.api.dto.PagamentoDTO;

import javax.validation.*;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PagamentoDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void valorValido() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("123");
        dto.setValor(BigDecimal.valueOf(300));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void valorInvalidoAbaixoDoMinimo() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("123");
        dto.setValor(BigDecimal.valueOf(3));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void valorInvalidoNulo() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("123");
        dto.setValor(null);

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
    
    @Test
    void valorExatamenteNoMinimo() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-1");
        dto.setValor(BigDecimal.valueOf(5.0));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void valorExatamenteNoMaximo() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-2");
        dto.setValor(BigDecimal.valueOf(5000.0));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void valorDecimalValido() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-3");
        dto.setValor(BigDecimal.valueOf(123.45));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void valorNegativo() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-4");
        dto.setValor(BigDecimal.valueOf(-10));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void valorZero() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-5");
        dto.setValor(BigDecimal.ZERO);

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void valorMaiorQueMaximo() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-6");
        dto.setValor(BigDecimal.valueOf(10000));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void valorMenorQueMinimo() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-7");
        dto.setValor(BigDecimal.valueOf(1.99));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void valorMuitoGrandePrecisaoAlta() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-8");
        dto.setValor(new BigDecimal("4999.9999"));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void valorComEscalaDeMoeda() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-9");
        dto.setValor(new BigDecimal("5.00"));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void valorMuitoPequenoComPrecisaoAlta() {
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPedidoId("pedido-10");
        dto.setValor(new BigDecimal("0.00001"));

        Set<ConstraintViolation<PagamentoDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

}
