package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;

import java.util.ArrayList;
import java.util.Collection;

@Mapper
public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attachmentsCount", ignore = true)
    AddNewMessageResponse addNewMessageRequestToAddNewMessageResponse(AddNewMessageRequest addNewMessageRequest);

    @Named("attachmentEntitiesToByteArrayCollection")
    @Mapping(target = "senderName", ignore = true)
    @Mapping(target = "receiverName", ignore = true)
    MessageDTO messageEntityToMessageDTO(MessageEntity messageEntity);

    default Collection<byte[]> attachmentEntitiesToByteArrayCollection(Collection<AttachmentEntity> attachmentEntities){
        Collection<byte[]> attachments = new ArrayList<>();

        attachmentEntities.forEach(ae -> attachments.add(ae.getAttachment()));

        return attachments;
    }
}
