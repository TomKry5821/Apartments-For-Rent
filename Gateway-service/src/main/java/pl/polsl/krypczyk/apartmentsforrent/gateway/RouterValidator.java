package pl.polsl.krypczyk.apartmentsforrent.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "user/api/v1/auth/register",
            "user/api/v1/auth/login",
            "announcement/api/v1/announcements",
            "announcement/api/v1/announcement/**"
    );

    public Boolean isSecured(ServerHttpRequest serverHttpRequest) {
        return openApiEndpoints
                .stream()
                .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
    }
}