package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@Builder
public class LoginUserResponse {

    private final UUID accessToken;
    private final String email;
    private final Collection<String> roles;
    private final LocalDateTime loginDate;
    private final Long id;

}
