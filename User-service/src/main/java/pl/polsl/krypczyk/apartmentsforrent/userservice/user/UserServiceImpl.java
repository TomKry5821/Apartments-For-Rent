package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.authorization.exception.AccountNotActiveException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.mapper.UserMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.ChangeUserDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public UserDetailsDTO getUserDetails(Long userId) {
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        var userDetails = user.getUserDetailsEntity();
        if (this.isAccountActive(userDetails))
            throw new AccountNotActiveException();

        var userDetailsDTO = userMapper.UserDetailsEntityToUserDetailsDTO(userDetails);
        return userDetailsDTO;
    }

    @Override
    public ChangeUserDetailsDTO changeUserDetails(ChangeUserDetailsDTO changeUserDetailsDTO, Long userId) {
        if (Objects.isNull(changeUserDetailsDTO) || Objects.isNull(userId) || userId < 1)
            throw new InvalidUserDetailsException();

        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();
        var userDetails = user.getUserDetailsEntity();

        if (this.isAccountActive(userDetails))
            throw new AccountNotActiveException();

        this.changeAndSaveUserDetails(userDetails, changeUserDetailsDTO);
        return changeUserDetailsDTO;
    }

    private void changeAndSaveUserDetails(UserDetailsEntity userDetailsEntity, ChangeUserDetailsDTO changeUserDetailsDTO) {
        var name = changeUserDetailsDTO.getName();
        if (!Objects.isNull(name))
            userDetailsEntity.setName(name);
        var surname = changeUserDetailsDTO.getSurname();
        if (!Objects.isNull(surname))
            userDetailsEntity.setSurname(surname);
        var email = changeUserDetailsDTO.getEmail();
        if (!Objects.isNull(email))
            userDetailsEntity.setEmail(email);
        var password = changeUserDetailsDTO.getPassword();
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
            throw new AccountNotActiveException();

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
