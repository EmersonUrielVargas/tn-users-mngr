package co.com.nequi.api.routers;

import co.com.nequi.api.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return RouterFunctions
            .route()
            .path("/api/v1",builder ->
                    builder.POST("/users/{id}", userHandler::postCreateUser))
            .build();
        }
}
