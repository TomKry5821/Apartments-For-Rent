package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain;

import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;

public interface EntityFactory {

    MessageEntity createMessageEntity(AddNewMessageRequest addNewMessageRequest);
}
