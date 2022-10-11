package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testGetUserDetailsWithValidUserIdAndRequesterIdShouldReturn200() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/2/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", 2))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserDetailsWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/100/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", 2))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetailsWithValidUserIdAndValidRequesterIdShouldReturn200() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        put("/user/api/v1/users/2/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Test",
                                            "surname": "Testowy",
                                            "email": "test@2test.pl",
                                            "password": "Test"
                                        }""")
                                .header("X-USER-ID", 2))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        put("/user/api/v1/users/10/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Test",
                                            "surname": "Testowy",
                                            "email": "test@test.pl",
                                            "password": "Test"
                                        }""")
                                .header("X-USER-ID", 2))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdAndInvalidRequesterIdShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        put("/user/api/v1/users/10/details")
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
    void testInactivateAccountWithValidUserIdAndValidRequesterIdShouldReturn204() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/2/inactivate")
                                .header("X-USER-ID", 2))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    void testInactivateAccountWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/192/inactivate")
                                .header("X-USER-ID", 2))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testInactivateAccountWithInvalidUserIdAndInvalidRequesterIdShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/192/inactivate")
                                .header("Authorization", "sdds"))
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