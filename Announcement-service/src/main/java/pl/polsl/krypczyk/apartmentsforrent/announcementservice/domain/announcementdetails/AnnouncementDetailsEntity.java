package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.nio.file.Path;
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

    @Column(name = "CONTENT", nullable = false)
    private String content;


}
