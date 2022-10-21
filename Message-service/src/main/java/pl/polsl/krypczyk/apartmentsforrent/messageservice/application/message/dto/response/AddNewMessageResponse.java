package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class AddNewMessageResponse {

    private Long id;

    private Long senderId;

    private Long receiverId;

    private String message;

    private Integer attachmentsCount;

}
