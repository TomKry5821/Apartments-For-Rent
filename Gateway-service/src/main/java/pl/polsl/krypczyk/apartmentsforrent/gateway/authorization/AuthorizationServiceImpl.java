package pl.polsl.krypczyk.apartmentsforrent.gateway.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.gateway.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.gateway.role.RolesDTO;
import pl.polsl.krypczyk.apartmentsforrent.gateway.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.gateway.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.gateway.userauthorization.UserAuthorizationRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserAuthorizationRepository userAuthorizationRepository;
    private final UserRepository userRepository;

    public RolesDTO authorizeByToken(UUID token) {
        UserAuthorizationEntity userAuthorizationEntity = userAuthorizationRepository.getUserAuthorizationEntityByToken(token);
        if (Objects.isNull(userAuthorizationEntity)) {
            return new RolesDTO(List.of(), -1L);
        }
        var user = this.userRepository.findUserEntityByUserAuthorizationEntity(userAuthorizationEntity);

        Collection<RoleEntity> userRoles = userAuthorizationEntity.getRoles();
        Collection<String> stringRoles = this.fillWithUserRoles(userRoles);

        return new RolesDTO(stringRoles, user.getId());
    }

    private Collection<String> fillWithUserRoles(Collection<RoleEntity> userRoles){
        Collection<String> stringRoles = new ArrayList<>();

        for (RoleEntity r : userRoles) {
            stringRoles.add(r.getName());
        }

        return stringRoles;
    }

}
