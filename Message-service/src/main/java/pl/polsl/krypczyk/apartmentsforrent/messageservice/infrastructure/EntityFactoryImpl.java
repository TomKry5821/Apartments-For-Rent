package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class EntityFactoryImpl implements EntityFactory {

    @Override
    public MessageEntity createMessageEntity(AddNewMessageRequest addNewMessageRequest,
                                             List<AttachmentEntity> attachments) {
        var message = new MessageEntity();

        message.setSendDate(LocalDateTime.now());
        message.setSenderId(addNewMessageRequest.getSenderId());
        message.setReceiverId(addNewMessageRequest.getReceiverId());
        message.setMessage(addNewMessageRequest.getMessage());
        message.setAttachments(attachments);

        return message;
    }


    @Override
    public AttachmentEntity createAttachmentEntity(MultipartFile file) {
        var attachment = new AttachmentEntity();
        try {
            attachment.setAttachment(file.getBytes());
        } catch (Exception e) {
            System.out.println("Attachment could not been saved in database");
        }
        return attachment;
    }
}
