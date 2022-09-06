package pl.polsl.krypczyk.apartmentsforrent.announcementservice.announcement;

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
@Table(name = "ANNOUNCEMENT")
public class AnnouncementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @OneToOne(orphanRemoval = true, optional = false)
    @JoinColumn(name = "ANNOUNCEMENT_DETAILS_ID", referencedColumnName = "ID")
    private AnnouncementDetailsEntity announcementDetailsEntity;


}
