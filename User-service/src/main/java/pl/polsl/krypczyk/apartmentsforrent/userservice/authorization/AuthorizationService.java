package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserDetailsDTOUserDetailsEntityMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserCreatedResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final RoleRepository roleRepository;

    private final UserDetailsDTOUserDetailsEntityMapper userDetailsDTOUserDetailsEntityMapper = Mappers.getMapper(UserDetailsDTOUserDetailsEntityMapper.class);

    public UserCreatedResponseDTO createUser(UserDetailsDTO userDetailsDTO) {
        Objects.requireNonNull(userDetailsDTO, "Invalid user data");

        RoleEntity roleEntity = this.createAndSaveUserRole();

        UserAuthorizationEntity userAuthorizationEntity = createAndSaveUserAuthorization();

        UserDetailsEntity userDetailsEntity = createAndSaveUserDetails(userDetailsDTO);

        UserEntity userEntity = this.createAndSaveUserEntity(userDetailsEntity, userAuthorizationEntity, List.of(roleEntity));

        return new UserCreatedResponseDTO(userDetailsDTO.getEmail(),
                userAuthorizationEntity.getToken(), userEntity.getRoles()
                .stream()
                .map(RoleEntity::getName).collect(Collectors.toList()));
    }

    private RoleEntity createAndSaveUserRole() {
        RoleEntity roleEntity = new RoleEntity("ROLE_USER");
        roleRepository.save(roleEntity);
        return roleEntity;
    }

    private UserAuthorizationEntity createAndSaveUserAuthorization() {
        UserAuthorizationEntity userAuthorizationEntity = new UserAuthorizationEntity();
        userAuthorizationEntity.setToken(UUID.randomUUID());
        userAuthorizationRepository.save(userAuthorizationEntity);
        return userAuthorizationEntity;
    }

    private UserDetailsEntity createAndSaveUserDetails(UserDetailsDTO userDetailsDTO) {
        UserDetailsEntity userDetailsEntity = this.userDetailsDTOUserDetailsEntityMapper.userDetailsDTOToUserDetailsEntity(userDetailsDTO);
        userDetailsEntity.setCreationDate(LocalDateTime.now());
        userDetailsRepository.save(userDetailsEntity);
        return userDetailsEntity;
    }

    private UserEntity createAndSaveUserEntity(UserDetailsEntity userDetailsEntity,
                                               UserAuthorizationEntity userAuthorizationEntity,
                                               Collection<RoleEntity> roleEntities) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserDetailsEntity(userDetailsEntity);
        userEntity.setUserAuthorizationEntity(userAuthorizationEntity);
        userEntity.setRoles(roleEntities);
        userRepository.save(userEntity);

        return userEntity;
    }
}
