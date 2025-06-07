package com.pagamento.common.enums;

/**
 * Enum de status HTTP personalizados com propósito descritivo.
 * Útil para enriquecer respostas ou logs em health checks e integrações externas.
 */
public enum CustomHttpStatus {

    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CONFLICT(409, "Conflict"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    UNKNOWN(-1, "Unknown Status");

    private final int code;
    private final String description;

    CustomHttpStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    public static CustomHttpStatus fromCode(int code) {
        for (CustomHttpStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return code + " - " + description;
    }
}
