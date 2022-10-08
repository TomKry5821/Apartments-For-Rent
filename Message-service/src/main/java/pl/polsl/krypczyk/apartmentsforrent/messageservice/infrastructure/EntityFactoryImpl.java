package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentRepository;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class EntityFactoryImpl implements EntityFactory {

    private final MessageRepository messageRepository;

    private final AttachmentRepository attachmentRepository;

    @Override
    public MessageEntity createMessageEntity(AddNewMessageRequest addNewMessageRequest) {
        var message = new MessageEntity();

        message.setSendDate(LocalDateTime.now());
        message.setSenderId(addNewMessageRequest.getSenderId());
        message.setReceiverId(addNewMessageRequest.getReceiverId());
        message.setMessage(addNewMessageRequest.getMessage());
        var attachments = new ArrayList<AttachmentEntity>();

        addNewMessageRequest.getAttachments().forEach(f -> attachments.add(this.createAttachmentEntity(f)));
        message.setAttachments(attachments);

        return this.messageRepository.save(message);
    }

    private AttachmentEntity createAttachmentEntity(MultipartFile file) {
        var attachment = new AttachmentEntity();
        try {
            attachment.setAttachment(file.getBytes());
            this.attachmentRepository.save(attachment);
        } catch (IOException e) {
            System.out.println("Attachment could not been saved in database");
        }
        return attachment;
    }
}
