package com.pagamento.common.validation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_REGEX =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern CNPJ_REGEX =
        Pattern.compile("^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$");

    private static final Pattern CVV_REGEX =
        Pattern.compile("^\\d{3,4}$");

    private static final Pattern PHONE_REGEX =
        Pattern.compile("^\\+?[0-9. ()-]{10,25}$");

    private static final Pattern CURRENCY_REGEX =
        Pattern.compile("^[A-Z]{3}$");

    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isValidCNPJ(String cnpj) {
        if (cnpj == null || !CNPJ_REGEX.matcher(cnpj).matches()) return false;
        cnpj = cnpj.replaceAll("[./-]", "");
        return cnpj.length() == 14;
    }

    public static boolean isValidCVV(String cvv) {
        return cvv != null && CVV_REGEX.matcher(cvv).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_REGEX.matcher(phone).matches();
    }

    public static boolean isValidCurrency(String currency) {
        return currency != null && CURRENCY_REGEX.matcher(currency).matches();
    }

    public static boolean isFutureDate(String date, String format) {
        if (date == null || format == null) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            return parsedDate.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isAmountWithinLimit(BigDecimal amount, BigDecimal min, BigDecimal max) {
        if (amount == null || min == null || max == null) return false;
        return amount.compareTo(min) >= 0 && amount.compareTo(max) <= 0;
    }
}
