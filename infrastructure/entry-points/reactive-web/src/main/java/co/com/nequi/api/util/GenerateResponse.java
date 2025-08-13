package co.com.nequi.api.util;

import co.com.nequi.api.constants.CommonConstants;
import co.com.nequi.model.enums.DomainMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class GenerateResponse {

    public static Mono<ServerResponse> generateErrorResponse(HttpStatus status, DomainMessage errorInfo) {
        return ServerResponse.status(status)
                .bodyValue(ErrorDto.builder()
                        .code(errorInfo.getCode())
                        .message(errorInfo.getMessage())
                        .externalCode(errorInfo.getExternalCode())
                        .build());
    }

    public static HttpStatus getStatusResponse(DomainMessage domainMessage) {
        return  switch (domainMessage.getCode()){
            case CommonConstants.HTTP_STATUS_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CommonConstants.HTTP_STATUS_BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.CONFLICT;
        };
    }
}
