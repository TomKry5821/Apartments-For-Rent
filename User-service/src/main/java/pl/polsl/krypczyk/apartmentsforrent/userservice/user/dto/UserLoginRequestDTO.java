package pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto;

import lombok.Data;

@Data
public class UserLoginRequestDTO {

    private final String email;

    private final String password;
}
