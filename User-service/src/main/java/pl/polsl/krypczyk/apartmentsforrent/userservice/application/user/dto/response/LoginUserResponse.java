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
public class LoginUserResponse {

    private UUID accessToken;
    private String email;
    private Collection<String> roles;
    private LocalDateTime loginDate;
    private Long id;

}
