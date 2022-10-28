package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@ToString
@Builder
public class MessageDTO {

    private Long senderId;

    private String senderName;

    private Long receiverId;

    private String receiverName;

    private String message;

    private LocalDateTime sendDate;

    @ToString.Exclude
    private Collection<byte[]> attachments;
}
