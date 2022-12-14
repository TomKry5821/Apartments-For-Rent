package pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ChangeUserDetailsResponse {

    private String name;

    private String surname;

    private String email;

    private String password;

}
