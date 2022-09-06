package pl.polsl.krypczyk.apartmentsforrent.announcementservice.announcementphoto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.announcementdetails.AnnouncementDetailsEntity;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "ANNOUNCEMENT_PHOTO")
public class AnnouncementPhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "PHOTO_PATH", nullable = false)
    private String photoPath;

    @ManyToOne(targetEntity = AnnouncementDetailsEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ANNOUNCEMENT_DETAILS_ID", referencedColumnName = "ID")
    @ToString.Exclude
    private AnnouncementDetailsEntity announcementDetailsEntity;

}