package com.pagamento.common.mapper;

import com.pagamento.common.dto.UserDTO;
import com.pagamento.common.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {PaymentMethodMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "paymentMethods", source = "paymentMethods")
    UserDTO toDTO(User user);

    @InheritInverseConfiguration
    @Mapping(target = "paymentMethods", ignore = true)
    User toEntity(UserDTO userDTO);

    @Named("uuidToString")
    static String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToUuid")
    static UUID stringToUuid(String uuidString) {
        try {
            return uuidString != null && !uuidString.isEmpty() ? 
                   UUID.fromString(uuidString) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}