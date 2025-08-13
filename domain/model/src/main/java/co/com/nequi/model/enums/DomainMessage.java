package co.com.nequi.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum DomainMessage {

    INTERNAL_ERROR("0-000", "0-000","Something went wrong, please try again"),
    USER_NOT_FOUND("0-001","0-001","The user was not found"),
    USER_CREATION_FAIL("0-002","0-002","An error occurred while creating the user"),
    ERROR_USER_ADAPTER("0-003","0-003","Something went wrong with the user adapter, please try again"),
    ERROR_DATA_USER_ADAPTER("0-004","0-004","There is an error with the request with the user adapter");

    private final String code;
    private final String externalCode;
    private final String message;

    public static DomainMessage findByExternalCode(String code) {
        return Arrays.stream(DomainMessage.values())
                .filter(msg -> msg.getExternalCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(INTERNAL_ERROR);
    }
}