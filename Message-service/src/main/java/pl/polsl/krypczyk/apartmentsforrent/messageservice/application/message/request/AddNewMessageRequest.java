package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Builder
public class AddNewMessageRequest {

    @NotNull
    @Min(value = 1, message = "Invalid sender id")
    private Long senderId;

    @NotNull
    @Min(value = 1, message = "Invalid receiver id")
    private Long receiverId;

    @NotNull(message = "Invalid message content")
    private String message;

    private Collection<MultipartFile> attachments;
}
