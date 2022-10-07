package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message;

import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.MessageDTO;

import java.util.Collection;

public interface MessageService {

    AddNewMessageResponse addNewMessage(AddNewMessageRequest addNewMessageRequest);

    Collection<MessageDTO> getConversation(Long senderId, Long receiverId);
}
