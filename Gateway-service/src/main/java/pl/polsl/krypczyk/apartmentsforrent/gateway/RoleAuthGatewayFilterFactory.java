package pl.polsl.krypczyk.apartmentsforrent.gateway;

import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RoleAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleAuthGatewayFilterFactory.Config> {

    private final RouterValidator routerValidator = new RouterValidator();

    @Data
    public static class Config {
    }

    public RoleAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            var response = exchange.getResponse();
            var requestToken = request.getHeaders().get("Authorization").get(0);

            if(!isResourceSecured(request)){
                return chain.filter(exchange);
            }
            if (!isAuthorizationKeyPresent(request)
                    || !TokenUtils.isTokenCorrect(requestToken)) {
                return setUnauthorizedResponse(response);
            }
            return chain.filter(exchange);
        };
    }

    private Boolean isResourceSecured(ServerHttpRequest serverHttpRequest){
        return routerValidator.isSecured(serverHttpRequest);
    }

    private Boolean isAuthorizationKeyPresent(ServerHttpRequest serverHttpRequest){
        return serverHttpRequest.getHeaders().containsKey("Authorization");
    }

    private Mono<Void> setUnauthorizedResponse(ServerHttpResponse serverHttpResponse){
        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        return serverHttpResponse.setComplete();
    }

}
