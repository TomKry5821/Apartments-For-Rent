package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageMapper;

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
}
