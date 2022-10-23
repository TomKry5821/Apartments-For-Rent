package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@Builder
@ToString
public class CreateUserResponse {

    private String email;
    private UUID accessToken;
    private Collection<String> roles;
    private LocalDateTime creationDate;
    private Long id;

}
