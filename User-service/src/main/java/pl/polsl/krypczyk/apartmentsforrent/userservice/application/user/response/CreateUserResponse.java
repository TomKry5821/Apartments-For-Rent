package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@Builder
public class CreateUserResponse {

    private final String email;
    private final UUID accessToken;
    private final Collection<String> roles;
    private final LocalDateTime creationDate;
    private final Long id;

}
