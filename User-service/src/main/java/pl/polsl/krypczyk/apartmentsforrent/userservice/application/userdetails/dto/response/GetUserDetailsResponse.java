package pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class GetUserDetailsResponse {

    private String name;

    private String surname;

    private String email;

    private Boolean isActive;

    private String password;

    private LocalDateTime creationDate;
}
