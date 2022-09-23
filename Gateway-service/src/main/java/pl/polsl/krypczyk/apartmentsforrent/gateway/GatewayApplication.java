package pl.polsl.krypczyk.apartmentsforrent.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import pl.polsl.krypczyk.apartmentsforrent.gateway.filter.AdminRoleAuthGatewayFilterFactory;
import pl.polsl.krypczyk.apartmentsforrent.gateway.filter.UserRoleAuthGatewayFilterFactory;

@SpringBootApplication
@RequiredArgsConstructor
public class GatewayApplication {

    private final UserRoleAuthGatewayFilterFactory userRoleAuthGatewayFilterFactory;
    private final AdminRoleAuthGatewayFilterFactory adminRoleAuthGatewayFilterFactory;

    @Value("${userHost}")
    private String USER_HOST;

    @Value("${announcementHost}")
    private String ANNOUNCEMENT_HOST;

    @Value("${messageHost}")
    private String MESSAGE_HOST;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r
                        .path("/user/api/v1/admin/**")
                        .filters(f ->
                                f.filter(adminRoleAuthGatewayFilterFactory.apply(new AdminRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://" + USER_HOST))
                .route(r -> r
                        .path("/user/api/v1/**")
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://" + USER_HOST))
                .route(r -> r
                        .path("/announcement/api/admin/v1/**")
                        .filters(f ->
                                f.filter(adminRoleAuthGatewayFilterFactory.apply(new AdminRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://" + ANNOUNCEMENT_HOST))
                .route(r -> r
                        .path("/announcement/api/v1/**")
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://" + ANNOUNCEMENT_HOST))
                .route(r -> r
                        .path("/message/api/admin/v1/**")
                        .filters(f ->
                                f.filter(adminRoleAuthGatewayFilterFactory.apply(new AdminRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://" + MESSAGE_HOST))
                .route(r -> r
                        .path("/message/api/v1/**")
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri("http://" + MESSAGE_HOST))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
