package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserCreatedResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsDTO;

public interface AuthorizationService {

     UserCreatedResponseDTO createUser(UserDetailsDTO userDetailsDTO);
}
