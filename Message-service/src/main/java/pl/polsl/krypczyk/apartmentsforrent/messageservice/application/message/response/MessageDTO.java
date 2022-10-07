package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@ToString
public class MessageDTO {

    private Long senderId;

    private Long receiverId;

    private String message;

    private LocalDateTime sendDate;

    private Collection<byte[]> attachments;
}
