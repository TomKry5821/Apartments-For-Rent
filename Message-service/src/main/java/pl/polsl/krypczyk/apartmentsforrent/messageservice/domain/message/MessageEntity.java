package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "MESSAGE")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "SENDER_ID", nullable = false)
    private Long senderId;

    @Column(name = "RECEIVER_ID", nullable = false)
    private Long receiverId;

    @Column(name = "SEND_DATE", nullable = false)
    private LocalDateTime sendDate;

    @Column(name = "CONTENT", nullable = false)
    private String message;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private Collection<AttachmentEntity> attachmentEntities;


}
