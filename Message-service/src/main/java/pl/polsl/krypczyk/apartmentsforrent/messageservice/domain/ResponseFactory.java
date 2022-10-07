package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain;


import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;

import java.util.Collection;

public interface ResponseFactory {

    AddNewMessageResponse createAddNewMessageResponse(AddNewMessageRequest addNewMessageRequest, Long messageId);

    Collection<MessageDTO> createGetConversationResponse(Collection<MessageEntity> messageEntities);

}
