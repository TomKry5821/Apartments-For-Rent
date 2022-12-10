package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.dto.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageRepository;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;

import java.util.List;

@SpringBootTest
@Transactional
class MessageServiceImplTest {

    private static final Long SENDER_ID = 1L;
    private static final Long RECEIVER_ID = 1L;
    private static final String CONTENT = "Content";

    @Autowired
    MessageService messageService;

    @Autowired
    MessageRepository messageRepository;

    @BeforeEach
    void deleteDbContent() {
        this.messageRepository.deleteAll();
    }

    @Test
    void testAddNewMessageWithValidMessageDataShouldReturnNotEmptyMessageFromRepository() {
        //GIVEN
        var addNewMessageRequest = this.addNewMessageRequest();

        //WHEN
        this.messageService.addNewMessage(addNewMessageRequest);

        //THEN
        Assertions.assertFalse(this.messageRepository.findAll().isEmpty());
    }

    @Test
    void testGetConversationWithNotEmptyConversationShouldReturnNotEmptyConversation() {
        //GIVEN
        var addNewMessageRequest = this.addNewMessageRequest();
        this.messageService.addNewMessage(addNewMessageRequest);

        //WHEN
        this.messageService.getConversation(SENDER_ID, RECEIVER_ID);

        //THEN
        Assertions.assertFalse(this.messageRepository.findAll().isEmpty());
    }

    @Test
    void testGetConversationWithEmptyConversationShouldReturnEmptyConversation() {
        //GIVEN
        //WHEN
        this.messageService.getConversation(SENDER_ID, RECEIVER_ID);

        //THEN
        Assertions.assertTrue(this.messageRepository.findAll().isEmpty());
    }

    @Test
    void testGetUserConversationsWithNotEmptyConversationShouldReturnNotEmptyConversationList(){
        //GIVEN
        var addNewMessageRequest = this.addNewMessageRequest();
        this.messageService.addNewMessage(addNewMessageRequest);

        //WHEN
        var conversations = this.messageService.getUserConversations(SENDER_ID);

        //THEN
        Assertions.assertFalse(conversations.isEmpty());
    }

    @Test
    void testGetUserConversationsWithEmptyConversationShouldReturnEmptyConversationList(){
        //GIVEN
        //WHEN
        var conversations = this.messageService.getUserConversations(SENDER_ID);

        //THEN
        Assertions.assertTrue(conversations.isEmpty());
    }

    private AddNewMessageRequest addNewMessageRequest() {
        return AddNewMessageRequest
                .builder()
                .receiverId(RECEIVER_ID)
                .senderId(SENDER_ID)
                .message(CONTENT)
                .attachments(List.of(new MockMultipartFile("test-file", "test content".getBytes())))
                .build();
    }

}