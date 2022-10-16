package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain;

import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;

public interface EntityFactory {

    MessageEntity createMessageEntity(AddNewMessageRequest addNewMessageRequest);

    AttachmentEntity createAttachmentEntity(MultipartFile file);
}
