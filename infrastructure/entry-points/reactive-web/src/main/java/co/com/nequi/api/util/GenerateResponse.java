package co.com.nequi.api.util;

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
                        .build());
    }
}
