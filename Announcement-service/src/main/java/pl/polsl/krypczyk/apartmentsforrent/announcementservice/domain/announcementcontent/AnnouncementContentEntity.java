package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "ANNOUNCEMENT_CONTENT")
public class AnnouncementContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<PhotoPathEntity> photoPaths;

}