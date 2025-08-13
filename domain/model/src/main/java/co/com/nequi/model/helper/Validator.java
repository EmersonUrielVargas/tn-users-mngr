package co.com.nequi.model.helper;

import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.BusinessException;

public class Validator {

    public static <T> void validateNotNull(T value, DomainMessage message) {
        if (value == null || (value instanceof String stringValue && (stringValue).isBlank())) {
            throw new BusinessException(message);
        }
    }

    public static void validatePositive(int value, DomainMessage message) {
        if (value < 0) {
            throw new BusinessException(message);
        }
    }
}
