package pl.polsl.krypczyk.apartmentsforrent.gateway.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.gateway.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.gateway.role.RolesDTO;
import pl.polsl.krypczyk.apartmentsforrent.gateway.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.gateway.userauthorization.UserAuthorizationRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserAuthorizationRepository userAuthorizationRepository;

    public RolesDTO authorizeByToken(UUID token) {
        UserAuthorizationEntity userAuthorizationEntity = userAuthorizationRepository.getUserAuthorizationEntityByToken(token);
        if (Objects.isNull(userAuthorizationEntity)) {
            return new RolesDTO(List.of());
        }

        Collection<RoleEntity> userRoles = userAuthorizationEntity.getUserEntity().getRoles();
        Collection<String> stringRoles = this.fillWithUserRoles(userRoles);

        return new RolesDTO(stringRoles);
    }

    private Collection<String> fillWithUserRoles(Collection<RoleEntity> userRoles){
        Collection<String> stringRoles = new ArrayList<>();

        for (RoleEntity r : userRoles) {
            stringRoles.add(r.getName());
        }

        return stringRoles;
    }

}
