package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageRepository;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final ResponseFactory responseFactory;

    private final EntityFactory entityFactory;

    private final MessageRepository messageRepository;

    @Override
    public AddNewMessageResponse addNewMessage(AddNewMessageRequest addNewMessageRequest) {
        log.info("Started adding message - " + addNewMessageRequest);

        var message = this.entityFactory.createMessageEntity(addNewMessageRequest);

        log.info("Successfully added message - " + message);
        return this.responseFactory.createAddNewMessageResponse(addNewMessageRequest, message.getId());
    }

    @Override
    @Transactional
    public Collection<MessageDTO> getConversation(Long senderId, Long receiverId) {
        log.info("Started retrieving conversation for sender with id " + senderId + " and receiver with id " + receiverId);

        var conversation = this.retrieveAndSortMessages(senderId, receiverId);

        log.info("Successfully retrieved conversation for sender with id " + senderId + " and receiver with id " + receiverId);
        return this.responseFactory.createGetConversationResponse(conversation);
    }

    List<MessageEntity> retrieveAndSortMessages(Long senderId, Long receiverId){
        var conversation = this.messageRepository.getMessageEntitiesBySenderIdAndReceiverId(senderId, receiverId);
        conversation.addAll(this.messageRepository.getMessageEntitiesBySenderIdAndReceiverId(receiverId, senderId));


        Comparator<MessageEntity> sendDateComparator = (m1, m2) -> m1.getSendDate().isAfter(m2.getSendDate()) ? -1 : m1.getSendDate().isBefore(m2.getSendDate()) ? 1 : 0;
        return conversation
                .stream()
                .sorted(sendDateComparator)
                .collect(Collectors.toList());
    }
}
