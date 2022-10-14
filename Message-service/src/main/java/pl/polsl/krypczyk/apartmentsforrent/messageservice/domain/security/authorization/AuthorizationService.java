package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.security.authorization;

import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.security.exception.UnauthorizedUserException;

public interface AuthorizationService {
    void authorizeUser(Long userId) throws UnauthorizedUserException;

    void authorizeAdmin() throws UnauthorizedUserException;
}
