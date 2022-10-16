package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        mvc.perform(
                post("/announcement/api/v1/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "userId":1,
                                   "title":"Title",
                                   "mainPhotoPath":"Main/photo/path",
                                   "roomsNumber":3,
                                   "rentalTerm":"2022-12-03",
                                   "caution":1000.00,
                                   "rentalAmount":1000.00,
                                   "content":"Content",
                                   "photoPaths":[
                                      "path/1",
                                      "path/2"
                                   ],
                                   "district":"District",
                                   "city":"City",
                                   "zipCode":"44-240",
                                   "street":"Street",
                                   "buildingNumber":"1A",
                                   "localNumber":3
                                }""")
                        .header("X-USER-ID", 1L)
                        .header("X-USER-ROLES", "[ROLE_ADMIN, ROLE_USER]"));
    }
}