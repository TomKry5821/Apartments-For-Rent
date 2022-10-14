package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.authorization.AuthorizationService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.authorization.exception.UnauthorizedUserException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    public void authorizeUser(Long userId) throws UnauthorizedUserException {
        var request = getRequest();
        var requesterId = this.getUserIdFromRequestOrThrowUnauthorizedException(request);
        var roles = this.getUserRolesFromRequest(request);

        log.info("Started user authorization with provided id - " + userId);

        if (!userId.equals(requesterId))
            throw new UnauthorizedUserException();

        if(!roles.contains(ROLE_USER))
            throw new UnauthorizedUserException();

        log.info("Successfully authorized user with provided id - " + userId);
    }

    @Override
    public void authorizeAdmin() throws UnauthorizedUserException {
        var request = getRequest();
        var roles = this.getUserRolesFromRequest(request);

        log.info("Started admin authorization");

        if(!roles.contains(ROLE_ADMIN))
            throw new UnauthorizedUserException();

        log.info("Successfully authorized admin");
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    private Long getUserIdFromRequestOrThrowUnauthorizedException(HttpServletRequest request) throws UnauthorizedUserException {
        try{
            return Long.parseLong(request.getHeader("X-USER-ID"));
        }catch(NumberFormatException e){
            throw new UnauthorizedUserException();
        }
    }

    private Collection<String> getUserRolesFromRequest(HttpServletRequest request) {

        var userRoles = request.getHeader("X-USER-ROLES")
                .replace("[", "")
                .replace("]", "")
                .split(", ");

        return List.of(userRoles);
    }
}
