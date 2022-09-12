package pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public void deleteUser(Long userId) {
        var user = this.userRepository.findUserEntityById(userId);
        if (Objects.isNull(user))
            throw new UserNotFoundException();

        this.userRepository.delete(user);
    }

    ////////////////////////////////////////////////
    /////////// FOR TESTS PURPOSE //////////////////
    public void deleteDbContent() {
        this.userRepository.deleteAll();
    }
}
