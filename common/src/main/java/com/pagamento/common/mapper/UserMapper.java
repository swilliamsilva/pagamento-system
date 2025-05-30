package com.pagamento.common.mapper;

import com.pagamento.common.dto.UserDTO;
import com.pagamento.common.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(target = "paymentMethods", expression = "java(mapPaymentMethods(user))")
    UserDTO toDTO(User user);

    default List<String> mapPaymentMethods(User user) {
        if (user.getPaymentMethods() == null) {
            return null;
        }
        return user.getPaymentMethods().stream()
                .map(pm -> pm.getType() + " - " + pm.getId().toString().substring(0, 8))
                .collect(Collectors.toList());
    }
}
