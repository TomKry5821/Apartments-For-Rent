package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageMapper;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class ResponseFactoryImpl implements ResponseFactory {

    private final MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);

    @Override
    public AddNewMessageResponse createAddNewMessageResponse(AddNewMessageRequest addNewMessageRequest, Long messageId) {
        var addNewMessageResponse = this.messageMapper.addNewMessageRequestToAddNewMessageResponse(addNewMessageRequest);
        addNewMessageResponse.setAttachmentsCount(addNewMessageRequest.getAttachments().size());
        addNewMessageResponse.setId(messageId);

        return addNewMessageResponse;
    }

    @Override
    public Collection<MessageDTO> createGetConversationResponse(Collection<MessageEntity> messageEntities) {
        Collection<MessageDTO> messageDTOS = new ArrayList<>();

        messageEntities.forEach(me -> messageDTOS.add(this.messageMapper.messageEntityToMessageDTO(me)));

        return messageDTOS;
    }
}
