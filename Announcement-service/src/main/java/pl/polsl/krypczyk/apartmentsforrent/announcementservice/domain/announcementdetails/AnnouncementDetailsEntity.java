package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementphoto.AnnouncementPhotoEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "ANNOUNCEMENT_DETAILS")
public class AnnouncementDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "MAIN_PHOTO_PATH", nullable = true)
    private String mainPhotoPath;

    @Column(name = "ROOMS_NUMBER", nullable = false)
    private Integer roomsNumber;

    @Column(name = "RENTAL_TERM", nullable = false)
    private LocalDate rentalTerm;

    @Column(name = "CAUTION", nullable = false)
    private BigDecimal caution;

    @Column(name = "RENTAL_AMOUNT", nullable = false)
    private BigDecimal rentalAmount;

    @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ADDRESS_DETAILS_ID", referencedColumnName = "ID")
    @ToString.Exclude
    AddressDetailsEntity addressDetailsEntity;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    Collection<AnnouncementPhotoEntity> announcementPhotos;


}
