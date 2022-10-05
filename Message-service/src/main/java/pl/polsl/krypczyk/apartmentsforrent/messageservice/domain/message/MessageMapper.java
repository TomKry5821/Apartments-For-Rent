package pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message;

import org.mapstruct.Mapper;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;

@Mapper
public interface MessageMapper {

    AddNewMessageResponse addNewMessageRequestToAddNewMessageResponse(AddNewMessageRequest addNewMessageRequest);
}
