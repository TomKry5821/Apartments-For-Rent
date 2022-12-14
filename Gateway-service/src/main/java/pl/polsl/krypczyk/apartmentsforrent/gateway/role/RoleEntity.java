package pl.polsl.krypczyk.apartmentsforrent.gateway.role;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Getter
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
}
