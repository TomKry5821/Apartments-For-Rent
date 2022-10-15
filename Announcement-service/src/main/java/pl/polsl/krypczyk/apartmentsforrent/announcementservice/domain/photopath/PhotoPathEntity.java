package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Getter
@Setter
@RequiredArgsConstructor
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
