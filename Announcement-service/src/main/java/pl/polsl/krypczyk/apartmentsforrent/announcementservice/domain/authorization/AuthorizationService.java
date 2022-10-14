package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.authorization;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.authorization.exception.UnauthorizedUserException;

public interface AuthorizationService {

    void authorizeUser(Long userId) throws UnauthorizedUserException;

    void authorizeAdmin() throws UnauthorizedUserException;
}
