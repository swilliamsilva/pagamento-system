package com.pagamento.common.mapper;

import com.pagamento.common.dto.PaymentMethodDTO;
import com.pagamento.common.enums.PaymentType;
import com.pagamento.common.model.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring",
        uses = UUIDMapper.class)
public interface PaymentMethodMapper {

    PaymentMethodMapper INSTANCE = Mappers.getMapper(PaymentMethodMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type", qualifiedByName = "mapPaymentType")
    @Mapping(target = "lastFourDigits", expression = "java(extractLastFourDigits(paymentMethod))")
    @Mapping(target = "pixKey", expression = "java(extractPixKey(paymentMethod))")
    @Mapping(target = "bankName", source = "bank")
    PaymentMethodDTO toDTO(PaymentMethod paymentMethod);

    @Named("mapPaymentType")
    default PaymentType mapPaymentType(PaymentMethod.Type type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case CREDIT_CARD: return PaymentType.CREDIT_CARD;
            case DEBIT_CARD: return PaymentType.DEBIT_CARD;
            case PIX: return PaymentType.PIX;
            case BOLETO: return PaymentType.BOLETO;
            case PLATFORM: return PaymentType.TRANSFER; // Mapeia PLATFORM para TRANSFER
            default: throw new IllegalArgumentException("Tipo de pagamento desconhecido: " + type);
        }
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
            return pm.getDetails();
        }
        return null;
    }
}