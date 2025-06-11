package com.pagamento.common.mapper;

import com.pagamento.common.dto.PaymentDTO;
import com.pagamento.common.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

/**
 * Mapper responsável pela conversão entre entidades de Pagamento e seus DTOs.
 */
@Mapper
public interface PaymentMapper {
    
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);
    
    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToString")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "paymentMethod.id", target = "paymentMethodId")
    @Mapping(source = "paymentMethod.type", target = "paymentType")
    PaymentDTO toDTO(Payment payment);
    
    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }
}