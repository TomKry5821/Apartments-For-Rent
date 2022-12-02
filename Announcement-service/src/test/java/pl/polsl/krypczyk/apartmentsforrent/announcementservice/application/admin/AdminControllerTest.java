package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.admin;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testDeleteAnnouncementWithValidAnnouncementIdShouldReturn204() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        delete("/announcement/api/v1/admin/announcements/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", 1L)
                                .header("X-USER-ROLES", "[ROLE_ADMIN, ROLE_USER]"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAnnouncementWithInvalidAnnouncementIdShouldReturn400() throws Exception {
        mvc.perform(
                        delete("/announcement/api/v1/admin/announcements/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", 1L)
                                .header("X-USER-ROLES", "[ROLE_ADMIN, ROLE_USER]"))
                .andExpect(status().isBadRequest());
    }

    private void createAnnouncement() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test".getBytes());
        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        mvc.perform(
                multipart("/announcement/api/v1/announcements")
                        .file("photos", multipartFile.getBytes())
                        .file("mainPhoto", multipartFile.getBytes())
                        .param("userId", "1")
                        .param("title", "Title")
                        .param("roomsNumber", "3")
                        .param("rentalTerm", "2022-12-03")
                        .param("caution", "1000.00")
                        .param("rentalAmount", "1000.00")
                        .param("content", "Content")
                        .param("district", "District")
                        .param("city", "city")
                        .param("zipCode", "44-240")
                        .param("street", "street")
                        .param("buildingNumber", "1A")
                        .param("localNumber", "1")
                        .header("X-USER-ID", 1L)
                        .header("X-USER-ROLES", "[ROLE_ADMIN, ROLE_USER]")
                        .contentType(mediaType)
                        .accept(MediaType.ALL));
    }
}