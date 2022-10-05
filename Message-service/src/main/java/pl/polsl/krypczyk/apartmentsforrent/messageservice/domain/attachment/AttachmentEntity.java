package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "ATTACHMENT")
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ATTACHMENT_PATH")
    @Lob
    private byte[] attachment;
}
