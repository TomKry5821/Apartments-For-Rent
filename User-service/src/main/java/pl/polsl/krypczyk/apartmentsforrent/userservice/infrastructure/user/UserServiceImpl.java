package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.user;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public GetUserDetailsResponse getUserDetails(Long userId) {
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();
        if (this.isAccountActive(userDetails))
            throw new InactiveAccountException();

        var userDetailsDTO = userMapper.UserDetailsEntityToUserDetailsDTO(userDetails);
        return userDetailsDTO;
    }

    @Override
    public ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId) {
        if (Objects.isNull(changeUserDetailsRequest) || Objects.isNull(userId) || userId < 1)
            throw new InvalidUserDetailsException();

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();
        var userDetails = user.getUserDetailsEntity();

        if (this.isAccountActive(userDetails))
            throw new InactiveAccountException();

        this.changeAndSaveUserDetails(userDetails, changeUserDetailsRequest);
        var changeUserDetailsResponse = this.userMapper.ChangeUserDetailsRequestToChangeUserDetailsResponse(changeUserDetailsRequest);
        return changeUserDetailsResponse;
    }

    private void changeAndSaveUserDetails(UserDetailsEntity userDetailsEntity, ChangeUserDetailsRequest changeUserDetailsRequest) {
        var name = changeUserDetailsRequest.getName();
        if (!Objects.isNull(name))
            userDetailsEntity.setName(name);
        var surname = changeUserDetailsRequest.getSurname();
        if (!Objects.isNull(surname))
            userDetailsEntity.setSurname(surname);
        var email = changeUserDetailsRequest.getEmail();
        if (!Objects.isNull(email))
            userDetailsEntity.setEmail(email);
        var password = changeUserDetailsRequest.getPassword();
        if (!Objects.isNull(password))
            userDetailsEntity.setPassword(password);

        this.userDetailsRepository.save(userDetailsEntity);
    }

    @Override
    public void inactivateAccount(Long userId){
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();
        if (this.isAccountActive(userDetails))
            throw new InactiveAccountException();

        userDetails.setIsActive(false);
        this.userDetailsRepository.save(userDetails);
    }

    private Boolean isAccountActive(UserDetailsEntity userDetails) {
        return userDetails.getIsActive().equals(false);
    }

    ////////////////////////////////////////////////
    /////////// FOR TESTS PURPOSE //////////////////
    public void deleteDbContent() {
        this.userRepository.deleteAll();
    }
}
