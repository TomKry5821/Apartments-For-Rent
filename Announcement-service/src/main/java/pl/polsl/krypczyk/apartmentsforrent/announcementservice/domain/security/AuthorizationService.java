package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.security;

import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.security.exception.UnauthorizedUserException;

public interface AuthorizationService {

    void authorizeUser(Long userId) throws UnauthorizedUserException;

    void authorizeAdmin() throws UnauthorizedUserException;
}
