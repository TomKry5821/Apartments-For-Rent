package pl.polsl.krypczyk.apartmentsforrent.messageservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    public void authorize(String... roles) {
        var request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();
        var userId = this.getUserIdFromRequest(request);
        log.info("Started authorization for user with id " + userId);

        var userRoles = this.getUserRolesFromRequest(request);

        for(var role : roles){
            if(userRoles.contains(role)) {
                log.info("Successfully authorized user with id " + userId);
                return;
            }
        }
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {

        return Long.parseLong(request.getHeader("X-USER-ID"));
    }

    private Collection<String> getUserRolesFromRequest(HttpServletRequest request) {

        var userRoles = request.getHeader("X-USER-ROLES")
                .replace("[", "")
                .replace("]", "")
                .split(", ");

        return List.of(userRoles);
    }
}
