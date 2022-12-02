package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "PHOTO")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PHOTO", nullable = false)
    @Lob
    private byte[] photo;
}
