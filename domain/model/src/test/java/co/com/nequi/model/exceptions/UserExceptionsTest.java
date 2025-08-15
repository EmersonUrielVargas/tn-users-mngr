package co.com.nequi.model.exceptions;

import co.com.nequi.model.enums.DomainMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserExceptionsTest {

    @Test
    void checkPocketExceptionTechnicalMessage() {

        UserException exception = new UserException(DomainMessage.INTERNAL_ERROR);

        assertNotNull(exception);
        assertEquals(DomainMessage.INTERNAL_ERROR, exception.getDomainMessage());
        assertEquals("0-000", exception.getDomainMessage().getCode());
        assertEquals("0-000", exception.getDomainMessage().getExternalCode());
        assertEquals("Something went wrong, please try again", exception.getDomainMessage().getMessage());
    }
}