package pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
public class UserLoggedInResponseDTO {

    private final UUID accessToken;
    private final String email;
    private final Collection<String> roles;
    private final LocalDateTime loginDate;
    private final Long id;

    public UserLoggedInResponseDTO(UUID accessToken, String email, Collection<String> roles, Long id){
        this.accessToken = accessToken;
        this.email = email;
        this.roles = roles;
        this.loginDate = LocalDateTime.now();
        this.id = id;
    }

}
