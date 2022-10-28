package pl.polsl.krypczyk.apartmentsforrent.gateway.role;

import lombok.Data;

import java.util.Collection;

@Data
public class RolesDTO {

    private Collection<String> roles;
    private Long userId;

    public RolesDTO(Collection<String> roles, Long userId) {
        this.roles = roles;
        this.userId = userId;
    }
}
