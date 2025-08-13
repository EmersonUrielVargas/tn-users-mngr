package co.com.nequi.api.routers;

import co.com.nequi.api.constants.CommonConstants;
import co.com.nequi.api.constants.SchemaConstants;
import co.com.nequi.api.dto.response.UserDetails;
import co.com.nequi.api.handler.UserHandler;
import co.com.nequi.api.util.ErrorDto;
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
                .GET("/api/v1/users", userHandler::getAllUsers,getAllUsersOpenAPI())
            .build();
    }

    private Consumer<Builder> getUserByIdOpenAPI() {
        return ops -> ops.tag(SchemaConstants.API_USER_TAG)
                .operationId(SchemaConstants.OPERATION_ID_FIND_USER_BY_ID).summary(SchemaConstants.SUMMARY_FIND_USER_BY_ID)
                .parameter(parameterBuilder()
                        .in(ParameterIn.PATH).name(CommonConstants.PATH_PARAM_ID).description(SchemaConstants.DESCRIPTION_PARAM_ID).required(true))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.OK.value()))
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_SUCCESSFUL)
                        .implementation(UserDetails.class))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .implementation(ErrorDto.class)
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_400))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .implementation(ErrorDto.class)
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_404))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .implementation(ErrorDto.class)
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_500));
    }

    private Consumer<Builder> postCreateUserOpenAPI() {
        return ops -> ops.tag(SchemaConstants.API_USER_TAG)
                .operationId(SchemaConstants.OPERATION_ID_CREATE_USER).summary(SchemaConstants.SUMMARY_CREATE_USER)
                .parameter(parameterBuilder()
                        .in(ParameterIn.PATH).name(CommonConstants.PATH_PARAM_ID).description(SchemaConstants.DESCRIPTION_PARAM_ID).required(true))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.CREATED.value()))
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_SUCCESSFUL)
                        .implementation(UserDetails.class))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .implementation(ErrorDto.class)
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_400))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .implementation(ErrorDto.class)
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_404))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .implementation(ErrorDto.class)
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_500));
    }

    private Consumer<Builder> getAllUsersOpenAPI() {
        return ops -> ops.tag(SchemaConstants.API_USER_TAG)
                .operationId(SchemaConstants.OPERATION_ID_GET_ALL_USERS)
                .summary(SchemaConstants.SUMMARY_GET_ALL_USERS)
                .parameter(parameterBuilder()
                        .in(ParameterIn.QUERY)
                        .name(CommonConstants.QUERY_PARAM_FILTER_NAME)
                        .description(SchemaConstants.DESCRIPTION_QUERY_PARAM_NAME)
                        .required(false))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.OK.value()))
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_SUCCESSFUL)
                        .implementationArray(UserDetails.class))
                .response(responseBuilder().responseCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .implementation(ErrorDto.class)
                        .description(SchemaConstants.DESCRIPTION_RESPONSE_500));
    }

}
