package com.pagamento.common.mapper;

import com.pagamento.common.dto.PaymentMethodSummaryDTO;
import com.pagamento.common.model.PaymentMethod;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMethodMapper {
    PaymentMethodMapper INSTANCE = Mappers.getMapper(PaymentMethodMapper.class);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    @Mapping(target = "lastFourDigits", expression = "java(extractLastFourDigits(paymentMethod))")
    @Mapping(target = "pixKey", expression = "java(extractPixKey(paymentMethod))")
    @Mapping(target = "bankName", source = "bank")
    PaymentMethodSummaryDTO toSummaryDTO(PaymentMethod paymentMethod);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    default String extractLastFourDigits(PaymentMethod pm) {
        if (pm.getType() == PaymentMethod.Type.CREDIT_CARD || 
            pm.getType() == PaymentMethod.Type.DEBIT_CARD) {
            return pm.getDetails() != null && pm.getDetails().length() > 4 
                   ? pm.getDetails().substring(pm.getDetails().length() - 4)
                   : null;
        }
        return null;
    }

    default String extractPixKey(PaymentMethod pm) {
        if (pm.getType() == PaymentMethod.Type.PIX) {
            // Implemente a lógica para extrair a chave PIX do JSON
            return pm.getDetails(); // Simplificado - implemente a lógica real
        }
        return null;
    }
}