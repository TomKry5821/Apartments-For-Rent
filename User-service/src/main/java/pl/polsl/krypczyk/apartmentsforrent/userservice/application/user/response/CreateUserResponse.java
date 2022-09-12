package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
public class CreateUserResponse {

    private final String email;
    private final UUID accessToken;
    private final Collection<String> roles;
    private final LocalDateTime creationDate;

    private final Long id;

    public CreateUserResponse(String email, UUID accessToken, Collection<String> roles, Long id) {
        this.email = email;
        this.accessToken = accessToken;
        this.roles = roles;
        this.creationDate = LocalDateTime.now();
        this.id = id;
    }
}
