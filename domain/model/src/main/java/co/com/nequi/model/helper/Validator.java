package co.com.nequi.model.helper;

import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.InvalidValueParamException;

public class Validator {

    public static <T> void validateNotNull(T value, DomainMessage message) {
        if (value == null) {
            throw new InvalidValueParamException(message);
        }else if (value instanceof String stringValue && (stringValue).isBlank()) {
            throw new InvalidValueParamException(message);
        }
    }

    public static void validatePositive(int value, DomainMessage message) {
        if (value < 0) {
            throw new InvalidValueParamException(message);
        }
    }
}
