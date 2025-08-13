package co.com.nequi.model.exceptions;

import co.com.nequi.model.enums.DomainMessage;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(DomainMessage domainMessage) {
        super(domainMessage);
    }
}
