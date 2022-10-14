package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security.config.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EntityFactoryImpl implements EntityFactory {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    @Override
    public RoleEntity createUserRoleEntity() {
        var role = new RoleEntity("ROLE_USER");
        return this.roleRepository.save(role);
    }

    @Override
    public UserAuthorizationEntity createUserAuthorizationEntity(RoleEntity roleEntity) {
        var userAuthorization = new UserAuthorizationEntity();
        userAuthorization.setToken(UUID.randomUUID());
        userAuthorization.setRoles(List.of(roleEntity));
        return this.userAuthorizationRepository.save(userAuthorization);
    }

    @Override
    public UserDetailsEntity createUserDetailsEntity(CreateUserRequest createUserRequest) {
        var userDetails = this.userMapper.userDetailsDTOToUserDetailsEntity(createUserRequest);
        userDetails.setCreationDate(LocalDateTime.now());
        userDetails.setPassword(AES.encrypt(userDetails.getPassword()));
        return this.userDetailsRepository.save(userDetails);
    }

    @Override
    public UserEntity createUserEntity(UserDetailsEntity userDetailsEntity, UserAuthorizationEntity userAuthorizationEntity) {
        var userEntity = new UserEntity();
        userEntity.setUserDetailsEntity(userDetailsEntity);
        userEntity.setUserAuthorizationEntity(userAuthorizationEntity);
        return userRepository.save(userEntity);
    }
}
