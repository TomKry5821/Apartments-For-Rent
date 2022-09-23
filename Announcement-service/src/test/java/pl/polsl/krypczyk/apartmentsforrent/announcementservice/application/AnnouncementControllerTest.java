package pl.polsl.krypczyk.apartmentsforrent.announcementservice.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnnouncementControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testGetAllAnnouncements_shouldReturn200() throws Exception {
        mvc.perform(
                        get("/announcement/api/v1/public/announcements")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddNewAnnouncement_WithValidBody_ShouldReturn200() throws Exception {
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
                                .header("requester-user-id",1L))
                .andExpect(status().isOk());
    }

    @Test
    void testAddNewAnnouncement_WithInvalidBody_ShouldReturn400() throws Exception {
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
                                .header("requester-user-id",1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAnnouncement_WithAllDetailsWithInvalidAnnouncementId_ShouldReturn400() throws Exception {
        mvc.perform(
                        get("/announcement/api/v1/public/announcements/10")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAnnouncement_WithAllDetailsWithValidAnnouncementId_ShouldReturn200() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        get("/announcement/api/v1/public/announcements/1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAnnouncement_WithValidBodyAndValidUserId_ShouldReturn200() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        put("/announcement/api/v1/announcements/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
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
                                .header("requester-user-id",1L))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAnnouncement_WithInvalidBodyAndValidUserId_ShouldReturn400() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        put("/announcement/api/v1/announcements/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                                 "title":"Title",
                                                 "mainPhotoPath":"Main/photo/path",
                                                 "roomsNumber":0,
                                                 "rentalTerm":"2022-12-03",
                                                 "caution":1000.00,
                                                 "rentalAmount":1000.00,
                                                 "content":"Content",
                                                 "photoPaths":[
                                                 "path/1",
                                                 "path/2"
                                                 ],
                                                 "district":null,
                                                 "city":"City",
                                                 "zipCode":"44-240",
                                                 "street":"Street",
                                                 "buildingNumber":"1A",
                                                 "localNumber":3
                                                 }""")
                                .header("requester-user-id",1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCloseAnnouncement_WithValidUserId_ShouldReturn204() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        post("/announcement/api/v1/announcements/1/close")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id",1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCloseAnnouncement_WithInvalidUserId_ShouldReturn400() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        post("/announcement/api/v1/announcements/10/close")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id",1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObserveAnnouncement_WithValidUserIdAndValidAnnouncementId_ShouldReturn201() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        post("/announcement/api/v1/announcements/1/observe/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id",1L))
                .andExpect(status().isCreated());
    }

    @Test
    void testObserveAnnouncement_WithInvalidUserIdAndValidAnnouncementId_ShouldReturn400() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        post("/announcement/api/v1/announcements/1/observe/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id",1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObserveAnnouncement_WithValidUserIdAndInvalidAnnouncementId_ShouldReturn400() throws Exception {
        this.createAnnouncement();

        mvc.perform(
                        post("/announcement/api/v1/announcements/100/observe/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id",1L))
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


