package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "OBSERVED_ANNOUNCEMENT")
public class ObservedAnnouncementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ANNOUNCEMENT_ID", referencedColumnName = "ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AnnouncementEntity announcementEntity;

    @Column(name = "OBSERVING_USER_ID", nullable = false)
    private Long observingUserId;

}
