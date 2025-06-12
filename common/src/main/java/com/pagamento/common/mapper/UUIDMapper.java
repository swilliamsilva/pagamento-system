package com.pagamento.common.mapper;

import java.util.UUID;

public class UUIDMapper {
    public static String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    public static UUID stringToUuid(String uuidString) {
        try {
            return uuidString != null && !uuidString.isEmpty() ? 
                   UUID.fromString(uuidString) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}