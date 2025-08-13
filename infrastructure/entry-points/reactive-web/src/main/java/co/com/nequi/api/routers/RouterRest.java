package co.com.nequi.api.routers;

import co.com.nequi.api.constants.CommonConstants;
import co.com.nequi.api.constants.OpenApiConstants;
import co.com.nequi.api.handler.UserHandler;
import co.com.nequi.api.util.ErrorDto;
import co.com.nequi.model.user.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API Users", version = "1.0", description = "Documentation APIs v1.0 for users"))
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return route()
                .GET("/api/v1/users/{id}", userHandler::getUserById,getUserByIdOpenAPI())
                .POST("/api/v1/users/{id}", userHandler::postCreateUser, postCreateUserOpenAPI())
            .build();
    }

    private Consumer<Builder> getUserByIdOpenAPI() {
        return ops -> ops.tag(OpenApiConstants.API_USER_TAG)
                .operationId(OpenApiConstants.OPERATION_ID_FIND_USER_BY_ID).summary(OpenApiConstants.SUMMARY_FIND_USER_BY_ID)
                .parameter(parameterBuilder()
                        .in(ParameterIn.PATH).name(CommonConstants.PATH_ID).description(OpenApiConstants.DESCRIPTION_PARAM_ID).required(true))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.OK))
                        .description(OpenApiConstants.DESCRIPTION_RESPONSE_SUCCESSFUL)
                        .implementation(User.class))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.BAD_REQUEST))
                        .implementation(ErrorDto.class)
                        .description(OpenApiConstants.DESCRIPTION_RESPONSE_400))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.BAD_REQUEST))
                        .implementation(ErrorDto.class)
                        .description(OpenApiConstants.DESCRIPTION_RESPONSE_404))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                        .implementation(ErrorDto.class)
                        .description(OpenApiConstants.DESCRIPTION_RESPONSE_500));
    }

    private Consumer<Builder> postCreateUserOpenAPI() {
        return ops -> ops.tag(OpenApiConstants.API_USER_TAG)
                .operationId(OpenApiConstants.OPERATION_ID_CREATE_USER).summary(OpenApiConstants.SUMMARY_CREATE_USER)
                .parameter(parameterBuilder()
                        .in(ParameterIn.PATH).name(CommonConstants.PATH_ID).description(OpenApiConstants.DESCRIPTION_PARAM_ID).required(true))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.CREATED))
                        .description(OpenApiConstants.DESCRIPTION_RESPONSE_SUCCESSFUL)
                        .implementation(User.class))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.BAD_REQUEST))
                        .implementation(ErrorDto.class)
                        .description(OpenApiConstants.DESCRIPTION_RESPONSE_400))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.BAD_REQUEST))
                        .implementation(ErrorDto.class)
                        .description(OpenApiConstants.DESCRIPTION_RESPONSE_404))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                        .implementation(ErrorDto.class)
                        .description(OpenApiConstants.DESCRIPTION_RESPONSE_500));
    }
}
