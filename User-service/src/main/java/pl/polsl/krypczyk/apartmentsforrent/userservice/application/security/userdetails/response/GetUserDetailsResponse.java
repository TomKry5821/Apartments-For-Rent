package pl.polsl.krypczyk.apartmentsforrent.userservice.application.security.userdetails.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class GetUserDetailsResponse {

    private String name;

    private String surname;

    private String email;

    private Boolean isActive;

    private String password;

    private LocalDateTime creationDate;
}
