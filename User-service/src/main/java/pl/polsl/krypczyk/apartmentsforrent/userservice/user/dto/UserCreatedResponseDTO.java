package pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
public class UserCreatedResponseDTO {

    private final String email;
    private final UUID accessToken;
    private final Collection<String> roles;
    private final LocalDateTime creationDate;

    public UserCreatedResponseDTO(String email, UUID accessToken, Collection<String> roles) {
        this.email = email;
        this.accessToken = accessToken;
        this.roles = roles;
        this.creationDate = LocalDateTime.now();
    }
}