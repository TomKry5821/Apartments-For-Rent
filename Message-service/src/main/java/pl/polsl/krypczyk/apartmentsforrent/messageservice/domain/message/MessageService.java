package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message;

import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;

public interface MessageService {

    AddNewMessageResponse addNewMessage(AddNewMessageRequest addNewMessageRequest);
}
