package pl.polsl.krypczyk.apartmentsforrent.gateway.authorization;

import pl.polsl.krypczyk.apartmentsforrent.gateway.role.RolesDTO;

import java.util.UUID;

public interface AuthorizationService {
    RolesDTO authorizeByToken(UUID token);
}
