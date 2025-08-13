package co.com.nequi.model.exceptions;

import co.com.nequi.model.enums.DomainMessage;
import lombok.Getter;

@Getter
public class InvalidValueParamException extends DomainException {

    public InvalidValueParamException(DomainMessage domainMessage) {
        super(domainMessage);
    }
}
