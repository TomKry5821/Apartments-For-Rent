package pl.polsl.krypczyk.apartmentsforrent.gateway.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserAuthorizationRepository userAuthorizationRepository;
    private final UserRepository userRepository;

    public RolesDTO authorizeByToken(UUID token) {
        log.info("Started authorizing user with provided access token - " + token);

        UserAuthorizationEntity userAuthorizationEntity = userAuthorizationRepository.getUserAuthorizationEntityByToken(token);
        if (Objects.isNull(userAuthorizationEntity)) {
            log.error("Authorization user with provided access token - " + token + " failed. User with provided token could not been found");
            return new RolesDTO(List.of(), -1L);
        }

        var user = this.userRepository.findUserEntityByUserAuthorizationEntity(userAuthorizationEntity);
        if (user.getUserDetailsEntity().getIsActive().equals(false)) {
            log.error("Authorization user with provided access token - " + token + " failed. User account is actually inactive");
            return new RolesDTO(List.of(), -1L);
        }

        Collection<RoleEntity> userRoles = userAuthorizationEntity.getRoles();
        Collection<String> stringRoles = this.fillWithUserRoles(userRoles);

        log.info("Successfully authorized user with provided access token - " + token + " with roles " + stringRoles);
        return new RolesDTO(stringRoles, user.getId());
    }

    private Collection<String> fillWithUserRoles(Collection<RoleEntity> userRoles) {
        Collection<String> stringRoles = new ArrayList<>();

        userRoles.forEach((ur) -> stringRoles.add(ur.getName()));

        return stringRoles;
    }

}
