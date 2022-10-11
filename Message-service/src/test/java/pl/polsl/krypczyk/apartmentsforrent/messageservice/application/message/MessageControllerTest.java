package pl.polsl.krypczyk.apartmentsforrent.messageservice.application.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
                        .contentType(mediaType)
                        .accept(MediaType.ALL))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetConversationWithValidSenderIdShouldReturn200() throws Exception {
        this.addNewMessage();

        mvc.perform(get("/message/api/v1/messages/1")
                        .header("X-USER-ID", 1L))
                .andExpect(status().isOk());

    }

    @Test
    void testGetConversationWithInvalidSenderIdShouldReturn401() throws Exception {
        this.addNewMessage();

        mvc.perform(get("/message/api/v1/messages/1")
                        .header("X-USER-ID", 0L))
                .andExpect(status().isOk());

    }

    void addNewMessage() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test".getBytes());

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        mvc.perform(multipart("/message/api/v1/messages").file("attachments", multipartFile.getBytes())
                .param("senderId", "0")
                .param("receiverId", "0")
                .param("message", "test")
                .contentType(mediaType)
                .accept(MediaType.ALL));
    }

}