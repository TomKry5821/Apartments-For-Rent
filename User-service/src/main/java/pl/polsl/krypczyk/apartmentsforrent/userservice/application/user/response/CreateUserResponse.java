package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@Builder
public class CreateUserResponse {

    private String email;
    private UUID accessToken;
    private Collection<String> roles;
    private LocalDateTime creationDate;
    private Long id;

}
