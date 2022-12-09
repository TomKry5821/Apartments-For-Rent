package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.HttpService;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageMapper;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseFactoryImpl implements ResponseFactory {

    private final MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);

    private final HttpService httpService;

    @Override
    public AddNewMessageResponse createAddNewMessageResponse(AddNewMessageRequest addNewMessageRequest, Long messageId) {
        var addNewMessageResponse = this.messageMapper.addNewMessageRequestToAddNewMessageResponse(addNewMessageRequest);
        addNewMessageResponse.setAttachmentsCount(Objects.requireNonNullElse(addNewMessageRequest.getAttachments(), Collections.emptyList()).size());
        addNewMessageResponse.setId(messageId);

        log.trace("Created add new message response - " + addNewMessageResponse);
        return addNewMessageResponse;
    }

    @Override
    public Collection<MessageDTO> createGetConversationResponse(Collection<MessageEntity> messageEntities, Long senderId, Long receiverId) {
        Collection<MessageDTO> messageDTOS = new ArrayList<>();
        var userNamesMap = this.createUsernamesMap(senderId, receiverId);

        messageEntities.forEach(me -> {
            var messageDTO = this.messageMapper.messageEntityToMessageDTO(me);
            messageDTO.setSenderName(userNamesMap.get(me.getSenderId()));
            messageDTO.setReceiverName(userNamesMap.get(me.getReceiverId()));
            messageDTOS.add(messageDTO);
        });

        log.trace("Created message DTOs - " + messageDTOS);
        return messageDTOS;
    }

    private Map<Long, String> createUsernamesMap(Long senderId, Long receiverId){
        var usernamesMap = new HashMap<Long, String>();
        var senderName =  this.httpService.retrieveUsernameFromUserService(senderId);
        var receiverName = this.httpService.retrieveUsernameFromUserService(receiverId);

        usernamesMap.put(senderId, senderName);
        usernamesMap.put(receiverId, receiverName);

        return usernamesMap;
    }
}
