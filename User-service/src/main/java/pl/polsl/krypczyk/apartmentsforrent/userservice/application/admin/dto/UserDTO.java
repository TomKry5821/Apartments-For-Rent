package pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Builder
public class UserDTO {

    private Long id;
    private UUID accessToken;
    private String name;
    private String surname;
    private String password;
    private String email;
    private LocalDateTime creationDate;
    private Boolean isActive;
    private Collection<String> roles;

}
