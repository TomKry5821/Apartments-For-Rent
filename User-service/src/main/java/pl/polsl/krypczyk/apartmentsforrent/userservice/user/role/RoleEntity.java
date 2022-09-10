package pl.polsl.krypczyk.apartmentsforrent.userservice.user.role;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "ROLE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    public RoleEntity(String roleName) {
        this.name = roleName;
    }
}
