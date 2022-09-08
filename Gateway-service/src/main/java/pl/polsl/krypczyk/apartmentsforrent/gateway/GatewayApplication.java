package pl.polsl.krypczyk.apartmentsforrent.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import pl.polsl.krypczyk.apartmentsforrent.gateway.filter.UserRoleAuthGatewayFilterFactory;

@SpringBootApplication
@RequiredArgsConstructor
public class GatewayApplication {

    private final UserRoleAuthGatewayFilterFactory userRoleAuthGatewayFilterFactory;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r
                        .path("/user/api/v1/**")
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://localhost:8081"))
                .route(r -> r
                        .path("/announcement/api/v1/**")
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://localhost:8082"))
                .route(r -> r
                        .path("/message/api/v1/**")
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://localhost:8083"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
