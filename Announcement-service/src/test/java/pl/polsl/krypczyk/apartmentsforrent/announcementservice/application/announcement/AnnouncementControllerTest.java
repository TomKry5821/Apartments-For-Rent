package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnnouncementControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getAllAnnouncements_shouldReturn200() throws Exception {
        mvc.perform(
                        get("/announcement/api/v1/public/announcements")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createAnnouncementWithValidBody_ShouldReturn200() throws Exception {
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
                                .header("Authorization", UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void createAnnouncementWithInvalidBody_ShouldReturn400() throws Exception {
        mvc.perform(
                        post("/announcement/api/v1/announcements")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                           "userId":null,
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
                                           "zipCode":"44q-240",
                                           "street":"Street",
                                           "buildingNumber":"1A",
                                           "localNumber":3
                                        }""")
                                .header("Authorization", UUID.randomUUID()))
                .andExpect(status().isBadRequest());
    }
}