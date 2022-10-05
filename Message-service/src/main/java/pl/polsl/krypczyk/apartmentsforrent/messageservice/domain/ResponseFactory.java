package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain;


import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;

public interface ResponseFactory {

    AddNewMessageResponse createAddNewMessageResponse(AddNewMessageRequest addNewMessageRequest, Long messageId);

}
