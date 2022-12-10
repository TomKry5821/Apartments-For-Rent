package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class MessageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testAddNewMessageWithValidDataShouldReturn204() throws Exception {

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test".getBytes());

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        mvc.perform(multipart("/message/api/v1/messages").file("attachments", multipartFile.getBytes())
                        .param("senderId", "1")
                        .param("receiverId", "1")
                        .param("message", "test")
                        .header("X-USER-ID", 1L)
                        .header("X-USER-ROLES", "ROLE_ADMIN, ROLE_USER]")
                        .contentType(mediaType)
                        .accept(MediaType.ALL))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAddNewMessageWithInvalidDataShouldReturn400() throws Exception {

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test".getBytes());

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        mvc.perform(multipart("/message/api/v1/messages").file("attachments", multipartFile.getBytes())
                        .param("senderId", "-1")
                        .param("receiverId", "-1")
                        .param("message", "test")
                        .header("X-USER-ID", 1L)
                        .header("X-USER-ROLES", "ROLE_ADMIN, ROLE_USER]")
                        .contentType(mediaType)
                        .accept(MediaType.ALL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetConversationWithValidSenderIdShouldReturn200() throws Exception {
        this.addNewMessage();

        mvc.perform(get("/message/api/v1/messages/1/conversation/1")
                        .header("X-USER-ID", 1L)
                        .header("X-USER-ROLES", "ROLE_ADMIN, ROLE_USER]"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserConversationsWithValidSenderIdShouldReturn200() throws Exception {
        this.addNewMessage();

        mvc.perform(get("/message/api/v1/messages/conversations/1")
                        .header("X-USER-ID", 1L)
                        .header("X-USER-ROLES", "ROLE_ADMIN, ROLE_USER]"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetConversationWithInvalidSenderIdShouldReturn401() throws Exception {
        this.addNewMessage();

        mvc.perform(get("/message/api/v1/messages/0/conversation/1")
                        .header("X-USER-ID", "K")
                        .header("X-USER-ROLES", "ROLE_ADMIN, ROLE_USER]"))
                .andExpect(status().isUnauthorized());

    }

    void addNewMessage() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test".getBytes());

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        mvc.perform(multipart("/message/api/v1/messages").file("attachments", multipartFile.getBytes())
                .param("senderId", "1")
                .param("receiverId", "1")
                .param("message", "test")
                .header("X-USER-ID", 1L)
                .header("X-USER-ROLES", "ROLE_ADMIN, ROLE_USER]")
                .contentType(mediaType)
                .accept(MediaType.ALL));
    }

}