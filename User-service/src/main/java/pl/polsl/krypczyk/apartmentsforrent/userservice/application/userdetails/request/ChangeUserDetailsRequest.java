package pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@NotNull
@Builder
public class ChangeUserDetailsRequest {

    private String name;

    private String surname;

    private String email;

    private String password;

}
