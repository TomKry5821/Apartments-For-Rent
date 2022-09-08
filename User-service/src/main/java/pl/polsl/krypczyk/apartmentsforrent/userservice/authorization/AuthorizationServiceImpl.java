package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserDetailsDTOUserDetailsEntityMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserCreatedResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoggedInResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoginRequestDTO;
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
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final RoleRepository roleRepository;

    private final UserDetailsDTOUserDetailsEntityMapper userDetailsDTOUserDetailsEntityMapper = Mappers.getMapper(UserDetailsDTOUserDetailsEntityMapper.class);

    public UserCreatedResponseDTO registerNewUser(UserDetailsDTO userDetailsDTO) throws BadCredentialsException, UserAlreadyExistsException {
        if(Objects.isNull(userDetailsDTO)){
            throw new BadCredentialsException();
        }
        if(this.userAlreadyExists(userDetailsDTO.getEmail())){
            throw new UserAlreadyExistsException();
        }

        RoleEntity role = this.createAndSaveUserRole();

        UserDetailsEntity userDetails = createAndSaveUserDetails(userDetailsDTO);

        UserEntity user = this.createAndSaveUserEntity(userDetails, List.of(role));

        UserAuthorizationEntity userAuthorization = createAndSaveUserAuthorization(user);

        return new UserCreatedResponseDTO(userDetailsDTO.getEmail(), userAuthorization.getToken(),
                user.getRoles()
                        .stream()
                        .map(RoleEntity::getName).collect(Collectors.toList())
        );
    }

    private Boolean userAlreadyExists(String email){
        return this.userDetailsRepository.existsByEmail(email);
    }

    private RoleEntity createAndSaveUserRole() {
        RoleEntity role = new RoleEntity("ROLE_USER");
        roleRepository.save(role);
        return role;
    }

    private UserAuthorizationEntity createAndSaveUserAuthorization(UserEntity userEntity) {
        UserAuthorizationEntity userAuthorization = new UserAuthorizationEntity();
        userAuthorization.setToken(UUID.randomUUID());
        userAuthorization.setUserEntity(userEntity);
        userAuthorizationRepository.save(userAuthorization);
        return userAuthorization;
    }

    private UserDetailsEntity createAndSaveUserDetails(UserDetailsDTO userDetailsDTO) {
        UserDetailsEntity userDetails = this.userDetailsDTOUserDetailsEntityMapper.userDetailsDTOToUserDetailsEntity(userDetailsDTO);
        userDetails.setCreationDate(LocalDateTime.now());
        userDetailsRepository.save(userDetails);
        return userDetails;
    }

    private UserEntity createAndSaveUserEntity(UserDetailsEntity userDetailsEntity,
                                               Collection<RoleEntity> roleEntities) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserDetailsEntity(userDetailsEntity);
        userEntity.setRoles(roleEntities);
        userRepository.save(userEntity);

        return userEntity;
    }

    @Override
    public UserLoggedInResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) throws UserNotFoundException, BadCredentialsException {
        if(Objects.isNull(userLoginRequestDTO)){
            throw new BadCredentialsException();
        }

        UserDetailsEntity userDetails = retrieveUserDetailsByEmailAndPassword(userLoginRequestDTO.getEmail(), userLoginRequestDTO.getPassword());

        UserEntity user = this.userRepository.findUserEntityByUserDetailsEntity(userDetails);

        UserAuthorizationEntity userAuthorization = this.userAuthorizationRepository.findUserAuthorizationEntityByUserEntity(user);

        this.updateAccessToken(userAuthorization);

        return new UserLoggedInResponseDTO(userAuthorization.getToken(), userDetails.getEmail(), user.getRoles().stream()
                .map(RoleEntity::getName).collect(Collectors.toList()));
    }

    private UserDetailsEntity retrieveUserDetailsByEmailAndPassword(String email, String password) throws UserNotFoundException {
        UserDetailsEntity userDetails = this.userDetailsRepository.findUserDetailsEntityByEmailAndPassword(email, password);
        if (Objects.isNull(userDetails)) {
            throw new UserNotFoundException();
        }

        return userDetails;
    }

    private void updateAccessToken(UserAuthorizationEntity userAuthorization) {
        userAuthorization.setToken(UUID.randomUUID());
        this.userAuthorizationRepository.save(userAuthorization);
    }
}
