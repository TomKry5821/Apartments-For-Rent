package pl.polsl.krypczyk.apartmentsforrent.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import pl.polsl.krypczyk.apartmentsforrent.gateway.filter.AdminRoleAuthGatewayFilterFactory;
import pl.polsl.krypczyk.apartmentsforrent.gateway.filter.UserRoleAuthGatewayFilterFactory;

@SpringBootApplication
@EnableEurekaClient
@RequiredArgsConstructor
public class GatewayApplication {

    public static final String PROTOCOL = "http://";
    public static final String USER_SERVICE_ADMIN_PATH = "/user/api/v1/admin/**";
    public static final String USER_SERVICE_PATH = "/user/api/v1/**";
    public static final String ANNOUNCEMENT_SERVICE_ADMIN_PATH = "/announcement/api/admin/v1/**";
    public static final String ANNOUNCEMENT_SERVICE_PATH = "/announcement/api/v1/**";
    public static final String MESSAGE_SERVICE_ADMIN_PATH = "/message/api/admin/v1/**";
    public static final String MESSAGE_SERVICE_PATH = "/message/api/v1/**";
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
                        .path(USER_SERVICE_ADMIN_PATH)
                        .filters(f ->
                                f.filter(adminRoleAuthGatewayFilterFactory.apply(new AdminRoleAuthGatewayFilterFactory.Config())))
                        .uri(PROTOCOL + USER_HOST))
                .route(r -> r
                        .path(USER_SERVICE_PATH)
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri(PROTOCOL + USER_HOST))
                .route(r -> r
                        .path(ANNOUNCEMENT_SERVICE_ADMIN_PATH)
                        .filters(f ->
                                f.filter(adminRoleAuthGatewayFilterFactory.apply(new AdminRoleAuthGatewayFilterFactory.Config())))
                        .uri(PROTOCOL + ANNOUNCEMENT_HOST))
                .route(r -> r
                        .path(ANNOUNCEMENT_SERVICE_PATH)
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri(PROTOCOL + ANNOUNCEMENT_HOST))
                .route(r -> r
                        .path(MESSAGE_SERVICE_ADMIN_PATH)
                        .filters(f ->
                                f.filter(adminRoleAuthGatewayFilterFactory.apply(new AdminRoleAuthGatewayFilterFactory.Config())))
                        .uri(PROTOCOL + MESSAGE_HOST))
                .route(r -> r
                        .path(MESSAGE_SERVICE_PATH)
                        .filters(f ->
                                f.filter(userRoleAuthGatewayFilterFactory.apply(new UserRoleAuthGatewayFilterFactory.Config())))
                        .uri(PROTOCOL + MESSAGE_HOST))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
