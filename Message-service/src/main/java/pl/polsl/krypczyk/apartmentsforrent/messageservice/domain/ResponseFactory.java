package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain;


import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.ConversationDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;

import java.util.Collection;

public interface ResponseFactory {

    AddNewMessageResponse createAddNewMessageResponse(AddNewMessageRequest addNewMessageRequest, Long messageId);

    Collection<MessageDTO> createGetConversationResponse(Collection<MessageEntity> messageEntities, Long senderId, Long receiverId);

    ConversationDTO createConversationDTO(Long userId, Long receiverId);

}
