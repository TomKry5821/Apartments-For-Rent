package pl.polsl.krypczyk.apartmentsforrent.userservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.authorization.AES;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.role.RoleRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userauthorization.UserAuthorizationRepository;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           UserDetailsRepository userDetailsRepository,
                           UserAuthorizationRepository userAuthorizationRepository,
                           RoleRepository roleRepository) {
        return args -> {
            UserEntity user = new UserEntity();
            UserDetailsEntity userDetails = setUserDetails();
            UserAuthorizationEntity userAuthorization = setUserAuthorization(roleRepository);
            userAuthorizationRepository.save(userAuthorization);
            userDetailsRepository.save(userDetails);
            user.setUserDetailsEntity(userDetails);
            user.setUserAuthorizationEntity(userAuthorization);
            userRepository.save(user);
        };
    }

    private UserDetailsEntity setUserDetails() {
        UserDetailsEntity userDetails = new UserDetailsEntity();
        userDetails.setCreationDate(LocalDateTime.now());
        userDetails.setEmail("admin@admin.com");
        userDetails.setName("Admin");
        userDetails.setSurname("Admin");
        userDetails.setPassword(AES.encrypt("Admin"));
        userDetails.setIsActive(true);

        return userDetails;
    }

    private UserAuthorizationEntity setUserAuthorization(RoleRepository roleRepository) {
        UserAuthorizationEntity userAuthorization = new UserAuthorizationEntity();
        userAuthorization.setToken(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        RoleEntity admin = new RoleEntity("ROLE_ADMIN");
        roleRepository.save(admin);
        RoleEntity user = new RoleEntity("ROLE_USER");
        roleRepository.save(user);
        userAuthorization.setRoles(List.of(admin, user));

        return userAuthorization;
    }
}
