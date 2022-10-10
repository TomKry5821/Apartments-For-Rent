package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testDeleteUserWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();
        //WHEN
        mvc.perform(
                        delete("/user/api/v1/admin/users/194")
                                .header("requester-user-id", 2L))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteUserWithInvalidUserIdAndInvalidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        delete("/user/api/v1/admin/users/194")
                                .header("Authorization", "sdsd"))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllUsersWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();
        //WHEN
        mvc.perform(
                        get("/user/api/v1/admin/users")
                                .header("requester-user-id", 2L))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllUsersWithInvalidUserIdAndInvalidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/admin/users")
                                .header("Authorization", "sdsd"))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();
        //WHEN
        mvc.perform(
                        put("/user/api/v1/admin/users/10/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Test",
                                            "surname": "Testowy",
                                            "email": "test@test.pl",
                                            "password": "Test"
                                        }""")
                                .header("requester-user-id", 2L))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdAndInvalidRequesterIdShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        put("/user/api/v1/admin/users/10/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Test",
                                            "surname": "Testowy",
                                            "email": "test@test.pl",
                                            "password": "Test"
                                        }""")
                                .header("Authorization", "sfsf"))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActivateAccountWithValidUserIdAndValidRequesterIdShouldReturn204() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/admin/users/1/activate")
                                .header("Authorization", "1")
                                .header("requester-user-id", 1L))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    void testActivateAccountWithInvalidUserIdAndValidRequesterIdShouldReturn204() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/admin/users/0/activate")
                                .header("Authorization", "1")
                                .header("requester-user-id", 1L))
                //THEN
                .andExpect(status().isBadRequest());
    }

    private void registerValidUser() throws Exception {
        mvc.perform(
                post("/user/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Test",
                                    "surname": "Testowy",
                                    "email": "test@test.pl",
                                    "isActive": true,
                                    "password": "Test"
                                }"""));
    }

}