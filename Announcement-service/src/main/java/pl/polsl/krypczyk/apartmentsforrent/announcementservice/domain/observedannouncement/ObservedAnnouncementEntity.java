package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Getter
@Setter
@RequiredArgsConstructor
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
