package co.com.nequi.sqs.sender;

import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.TechnicalException;
import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserNotificationGateway;
import co.com.nequi.sqs.sender.config.SQSSenderProperties;
import co.com.nequi.sqs.sender.constants.SqsConstants;
import co.com.nequi.sqs.sender.util.UserSqsUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.Objects;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements UserNotificationGateway {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper objectMapper;

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<String> notifyNewUser(User user) {
        return Mono.just(user)
                .map(UserSqsUtilities::parserToStringSQS)
                .flatMap(this::send)
                .doOnSuccess(messageResponse -> log.info(SqsConstants.LOG_SEND_NEW_USER_SUCCESSFUL, kv(SqsConstants.LOG_RESPONSE_SUCCESSFUL_KEY, messageResponse)))
                .doOnError(exception -> log.info(SqsConstants.LOG_SEND_NEW_USER_ERROR, kv(SqsConstants.LOG_RESPONSE_ERROR_KEY, exception)))
                .onErrorMap(exception -> new TechnicalException(exception,DomainMessage.INTERNAL_ERROR));
    }
}
