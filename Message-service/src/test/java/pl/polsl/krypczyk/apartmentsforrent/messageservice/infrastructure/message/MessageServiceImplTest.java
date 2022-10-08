package pl.polsl.krypczyk.apartmentsforrent.messageservice.infrastructure.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.request.AddNewMessageRequest;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message.response.AddNewMessageResponse;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageRepository;
import pl.polsl.krypczyk.apartmentsforrent.messageservice.domain.message.MessageService;

import java.util.List;

@SpringBootTest
class MessageServiceImplTest {

    @Autowired
    MessageService messageService;

    @Autowired
    MessageRepository messageRepository;

    @BeforeEach
    void deleteDbContent() {
        this.messageRepository.deleteAll();
    }

    @Test
    void testAddNewMessage_WithValidMessageData() {
        //GIVEN
        var addNewMessageRequest = this.addNewMessageRequest();
        var expected = this.addNewMessageResponse();

        //WHEN
        var actual = this.messageService.addNewMessage(addNewMessageRequest);

        //THEN
        Assertions.assertEquals(expected, actual);

    }

    @Test
    @Transactional
    void testGetConversation_WithNotEmptyConversation() {
        //GIVEN
        var addNewMessageRequest = this.addNewMessageRequest();
        this.messageService.addNewMessage(addNewMessageRequest);

        //WHEN
        this.messageService.getConversation(1L, 1L);

        //THEN
        Assertions.assertFalse(this.messageRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    void testGetConversation_WithEmptyConversation() {
        //GIVEN

        //WHEN
        this.messageService.getConversation(1L, 1L);

        //THEN
        Assertions.assertTrue(this.messageRepository.findAll().isEmpty());
    }

    private AddNewMessageRequest addNewMessageRequest() {
        return AddNewMessageRequest
                .builder()
                .receiverId(1L)
                .senderId(1L)
                .message("Test content")
                .attachments(List.of(new MockMultipartFile("test-file", "test content".getBytes())))
                .build();
    }

    private AddNewMessageResponse addNewMessageResponse() {
        return AddNewMessageResponse
                .builder()
                .id(1L)
                .message("Test content")
                .attachmentsCount(1)
                .senderId(1L)
                .receiverId(1L)
                .build();
    }

}