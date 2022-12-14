package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.ConversationDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentRepository;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageRepository;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MessageServiceImpl implements MessageService {

    private final ResponseFactory responseFactory;

    private final AttachmentRepository attachmentRepository;

    private final EntityFactory entityFactory;

    private final MessageRepository messageRepository;

    @Override
    public AddNewMessageResponse addNewMessage(AddNewMessageRequest addNewMessageRequest) {
        log.info("Started adding message - " + addNewMessageRequest);

        var attachments = new ArrayList<AttachmentEntity>();
        Objects.requireNonNullElse(addNewMessageRequest.getAttachments(), new ArrayList<MultipartFile>())
                .forEach(f ->
                        attachments.add(this.attachmentRepository.save(this.entityFactory.createAttachmentEntity(f))));

        var message = this.entityFactory.createMessageEntity(addNewMessageRequest, attachments);
        this.messageRepository.save(message);

        log.info("Successfully added message - " + message);
        return this.responseFactory.createAddNewMessageResponse(addNewMessageRequest, message.getId());
    }

    @Override
    public Collection<MessageDTO> getConversation(Long senderId, Long receiverId) {
        log.info("Started retrieving conversation for sender with id " + senderId + " and receiver with id " + receiverId);

        var conversation = this.retrieveAndSortMessages(senderId, receiverId);

        log.info("Successfully retrieved conversation for sender with id " + senderId + " and receiver with id " + receiverId);
        return this.responseFactory.createGetConversationResponse(conversation, senderId, receiverId);
    }

    List<MessageEntity> retrieveAndSortMessages(Long senderId, Long receiverId) {
        var conversation = this.messageRepository.getMessageEntitiesBySenderIdAndReceiverId(senderId, receiverId);
        conversation.addAll(this.messageRepository.getMessageEntitiesBySenderIdAndReceiverId(receiverId, senderId));


        Comparator<MessageEntity> sendDateComparator = (m1, m2) -> m1.getSendDate().isBefore(m2.getSendDate()) ? -1 : m1.getSendDate().isBefore(m2.getSendDate()) ? 1 : 0;
        return conversation
                .stream()
                .sorted(sendDateComparator)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ConversationDTO> getUserConversations(Long userId) {
        log.info("Started retrieving all conversations for user with id - " + userId);
        var receiverIds = this.messageRepository.getAllReceiversBySenderId(userId);
        receiverIds.addAll(this.messageRepository.getAllSendersByReceiverId(userId));
        var userConversations = receiverIds
                .stream()
                .map(rId -> this.responseFactory.createConversationDTO(userId, rId))
                .collect(Collectors.toList());

        log.info("Successfully retrieved all conversations for user with id - " + userId);
        return userConversations;
    }
}
