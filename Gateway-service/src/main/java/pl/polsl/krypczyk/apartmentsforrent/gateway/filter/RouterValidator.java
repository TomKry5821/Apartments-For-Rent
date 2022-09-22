package pl.polsl.krypczyk.apartmentsforrent.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = List.of(
            "user/api/v1/auth/register",
            "user/api/v1/auth/login",
            "announcement/api/v1/public/announcements",
            "announcement/api/v1/public/announcements/"
    );

    public Boolean isSecured(ServerHttpRequest serverHttpRequest) {
        return openApiEndpoints
                .stream()
                .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
    }
}