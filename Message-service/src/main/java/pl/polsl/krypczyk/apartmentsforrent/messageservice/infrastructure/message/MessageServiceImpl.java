package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageMapper;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageRepository;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final ResponseFactory responseFactory;

    private final EntityFactory entityFactory;

    @Override
    public AddNewMessageResponse addNewMessage(AddNewMessageRequest addNewMessageRequest) {
        log.info("Started adding message - " + addNewMessageRequest);

        var message = this.entityFactory.createMessageEntity(addNewMessageRequest);

        log.info("Successfully added message - " + message);
        return this.responseFactory.createAddNewMessageResponse(addNewMessageRequest, message.getId());
    }
}
