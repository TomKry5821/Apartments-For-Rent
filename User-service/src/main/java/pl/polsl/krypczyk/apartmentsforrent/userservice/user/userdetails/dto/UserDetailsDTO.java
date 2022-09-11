package pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserDetailsDTO {

    private String name;

    private String surname;

    private String email;

    private Boolean isActive;

    private String password;

    private LocalDateTime creationDate;
}
