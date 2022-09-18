package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "PHOTO_PATH")
public class PhotoPathEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PHOTO_PATH", nullable = false)
    private String photoPath;
}
