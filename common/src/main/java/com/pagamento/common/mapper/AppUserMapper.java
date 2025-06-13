package com.pagamento.common.mapper;

import com.pagamento.common.dto.AppUserDTO;
import com.pagamento.common.model.AppUser;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import com.pagamento.common.mapper.AppUserMapper.UUIDMapper;


@Mapper(componentModel = "spring",
        uses = {PaymentMethodMapper.class, UUIDMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppUserMapper {

    public class UUIDMapper {

	}

	@Mapping(target = "id", source = "id")
    @Mapping(target = "paymentMethods", source = "paymentMethods")
    AppUserDTO toDTO(AppUser appUser);

    @InheritInverseConfiguration
    @Mapping(target = "paymentMethods", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);
}