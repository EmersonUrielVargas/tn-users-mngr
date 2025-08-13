package co.com.nequi.model.exceptions;

import co.com.nequi.model.enums.DomainMessage;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final DomainMessage domainMessage;

    public DomainException(DomainMessage domainMessage) {
        super(domainMessage.getMessage());
        this.domainMessage = domainMessage;
    }
}
