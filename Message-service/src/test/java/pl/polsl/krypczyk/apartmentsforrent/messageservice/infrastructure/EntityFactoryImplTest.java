package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.attachment.AttachmentEntity;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageEntity;


import java.util.Collection;
import java.util.Collections;

@SpringBootTest
class EntityFactoryImplTest {

    private static final String MESSAGE = "Test";
    private static final Long SENDER_ID = 1L;
    private static final Long RECEIVER_ID = 2L;
    private static final Collection<AttachmentEntity> DB_ATTACHMENTS = Collections.emptyList();
    private static final Collection<MultipartFile> REQUEST_ATTACHMENTS = Collections.emptyList();
    @Autowired
    EntityFactory entityFactory;


    @Test
    void testCreateMessageEntityWithValidAddNewMessageRequestShouldReturnEqualExpectedMessageEntity() {
        //GIVEN
        var addNewMessageRequest = validAddNewMessageRequest();
        var expected = validMessageEntity();

        //WHEN
        var actual = entityFactory.createMessageEntity(addNewMessageRequest, DB_ATTACHMENTS.stream().toList());

        //THEN
        Assertions.assertEquals(expected.getMessage(), actual.getMessage());
        Assertions.assertEquals(expected.getSenderId(), actual.getSenderId());
        Assertions.assertEquals(expected.getReceiverId(), actual.getReceiverId());
        Assertions.assertEquals(expected.getReceiverId(), actual.getReceiverId());
        Assertions.assertEquals(expected.getAttachments(), actual.getAttachments());
    }

    @Test
    void testCreateAttachmentEntityWithInvalidAddAttachmentShouldAddEmptyAttachment() {
        //GIVEN
        //WHEN
        var underTest = this.entityFactory.createAttachmentEntity(null);

        //THEN
        Assertions.assertEquals(underTest.getAttachment(), new AttachmentEntity().getAttachment());
    }

    private MessageEntity validMessageEntity() {
        var message = new MessageEntity();
        message.setMessage(MESSAGE);
        message.setSenderId(SENDER_ID);
        message.setReceiverId(RECEIVER_ID);
        message.setAttachments(DB_ATTACHMENTS);

        return message;
    }

    private AddNewMessageRequest validAddNewMessageRequest() {
        return AddNewMessageRequest
                .builder()
                .message(MESSAGE)
                .senderId(SENDER_ID)
                .receiverId(RECEIVER_ID)
                .attachments(REQUEST_ATTACHMENTS)
                .build();
    }
}