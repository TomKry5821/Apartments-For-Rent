package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Lob;

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

    @Column(name = "ATTACHMENT")
    @Lob
    private byte[] attachment;
}
