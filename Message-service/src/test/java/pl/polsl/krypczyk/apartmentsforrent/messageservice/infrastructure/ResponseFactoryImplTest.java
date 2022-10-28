package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.response.MessageDTO;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;


import java.util.Collection;
import java.util.Collections;
import java.util.List;


@SpringBootTest
class ResponseFactoryImplTest {

    private static final String MESSAGE = "Test";
    private static final Long MESSAGE_ID = 1L;
    private static final Long SENDER_ID = 1L;
    private static final Long RECEIVER_ID = 2L;
    private static final Integer ATTACHMENTS_COUNT = 1;
    private static final Collection<MultipartFile> REQUEST_ATTACHMENTS = Collections.emptyList();
    private static final Collection<AttachmentEntity> DB_ATTACHMENTS = Collections.emptyList();
    private static final Collection<byte[]> BYTE_ATTACHMENTS = Collections.emptyList();

    @Autowired
    private ResponseFactory responseFactory;

    @Test
    void testCreateAddNewMessageResponseWithValidAddNewMessageRequestShouldReturnExpectedResponse() {
        //GIVEN
        var messageRequest = validAddMessageRequest();
        var expected = validAddNewMessageResponse();

        //WHEN
        var actual = this.responseFactory.createAddNewMessageResponse(messageRequest, MESSAGE_ID);

        //THEN
        Assertions.assertEquals(expected.getMessage(), actual.getMessage());
        Assertions.assertEquals(expected.getSenderId(), actual.getSenderId());
        Assertions.assertEquals(expected.getReceiverId(), actual.getReceiverId());
        Assertions.assertEquals(expected.getAttachmentsCount(), expected.getAttachmentsCount());
    }

    @Test
    void testCreateGetConversationResponseWithValidAddNewMessageRequestShouldReturnEqualExpectedMessageEntity() {
        //GIVEN
        var messages = List.of(validMessageEntity());
        var expected = List.of(validMessageDTO());

        //WHEN
        var actual = responseFactory.createGetConversationResponse(messages, messages.get(0).getSenderId(), messages.get(0).getReceiverId());

        //THEN
        Assertions.assertEquals(expected.get(0).getMessage(), actual.stream().toList().get(0).getMessage());
        Assertions.assertEquals(expected.get(0).getSenderId(), actual.stream().toList().get(0).getSenderId());
        Assertions.assertEquals(expected.get(0).getReceiverId(), actual.stream().toList().get(0).getReceiverId());
        Assertions.assertEquals(expected.get(0).getAttachments(), actual.stream().toList().get(0).getAttachments());
    }

    private AddNewMessageRequest validAddMessageRequest() {
        return AddNewMessageRequest
                .builder()
                .message(MESSAGE)
                .senderId(SENDER_ID)
                .receiverId(RECEIVER_ID)
                .attachments(REQUEST_ATTACHMENTS)
                .build();
    }

    private AddNewMessageResponse validAddNewMessageResponse() {
        return AddNewMessageResponse
                .builder()
                .message(MESSAGE)
                .senderId(SENDER_ID)
                .receiverId(RECEIVER_ID)
                .attachmentsCount(ATTACHMENTS_COUNT)
                .build();
    }

    private MessageDTO validMessageDTO() {
        return MessageDTO
                .builder()
                .message(MESSAGE)
                .senderId(SENDER_ID)
                .receiverId(RECEIVER_ID)
                .attachments(BYTE_ATTACHMENTS)
                .build();
    }

    private MessageEntity validMessageEntity(){
        var message = new MessageEntity();
        message.setMessage(MESSAGE);
        message.setSenderId(SENDER_ID);
        message.setReceiverId(RECEIVER_ID);
        message.setAttachments(DB_ATTACHMENTS);

        return message;
    }

}