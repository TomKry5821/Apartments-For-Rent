package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;

import java.util.ArrayList;
import java.util.Collection;

@Mapper
public interface MessageMapper {

    AddNewMessageResponse addNewMessageRequestToAddNewMessageResponse(AddNewMessageRequest addNewMessageRequest);

    @Named("attachmentEntitiesToByteArrayCollection")
    MessageDTO messageEntityToMessageDTO(MessageEntity messageEntity);

    default Collection<byte[]> attachmentEntitiesToByteArrayCollection(Collection<AttachmentEntity> attachmentEntities){
        Collection<byte[]> attachments = new ArrayList<>();

        attachmentEntities.forEach(ae -> attachments.add(ae.getAttachment()));

        return attachments;
    }
}
