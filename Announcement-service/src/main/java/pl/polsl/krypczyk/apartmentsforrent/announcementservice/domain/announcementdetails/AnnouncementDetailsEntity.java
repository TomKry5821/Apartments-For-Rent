package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column(name = "MAIN_PHOTO")
    @Lob
    private byte[] mainPhoto;

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

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @JoinColumn(name = "ANNOUNCEMENT_CONTENT_ID", referencedColumnName = "ID")
    @ToString.Exclude
    AnnouncementContentEntity announcementContent;


}
