package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testDeleteAnnouncement_WithValidAnnouncementId_ShouldReturn204() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        delete("/announcement/api/v1/admin/announcements/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAnnouncement_WithInvalidAnnouncementId_ShouldReturn400() throws Exception {
        mvc.perform(
                        delete("/announcement/api/v1/admin/announcements/10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id", 1L))
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
                        .header("requester-user-id", 1L));
    }
}