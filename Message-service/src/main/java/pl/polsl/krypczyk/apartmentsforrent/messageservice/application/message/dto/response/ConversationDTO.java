package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ConversationDTO {

    private Long senderId;

    private String senderName;

    private Long receiverId;

    private String receiverName;

}
