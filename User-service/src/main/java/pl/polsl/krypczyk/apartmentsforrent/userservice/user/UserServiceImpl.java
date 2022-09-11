package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.mapper.UserMapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserAuthorizationRepository userAuthorizationRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public UserDetailsDTO getUserDetails(UUID accessToken) {
        var userAuthorization = this.findUserAuthorizationByAccessToken(accessToken);
        if (Objects.isNull(userAuthorization)) {
            throw new UserNotFoundException();
        }
        return null;
    }

    private UserAuthorizationEntity findUserAuthorizationByAccessToken(UUID accessToken) {
        return this.userAuthorizationRepository.findUserAuthorizationEntityByToken(accessToken);
    }
}
