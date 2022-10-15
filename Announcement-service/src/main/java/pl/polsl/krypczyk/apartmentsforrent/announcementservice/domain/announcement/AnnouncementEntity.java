package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "ANNOUNCEMENT")
public class AnnouncementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDate creationDate;

    @Column(name = "IS_CLOSED", nullable = false)
    private Boolean isClosed;

    @Column(name = "DISTRICT", nullable = false)
    private String district;

    @Column(name = "CITY", nullable = false)
    private String city;

    @OneToOne(orphanRemoval = true, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ANNOUNCEMENT_DETAILS_ID", referencedColumnName = "ID")
    private AnnouncementDetailsEntity announcementDetailsEntity;


}
