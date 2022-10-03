package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testGetUserDetails_WithValidUserIdAndRequesterId_shouldReturn200() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/2/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id", 2))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserDetails_WithInvalidUserIdAndValidRequesterId_shouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/100/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id", 2))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetails_WithValidUserIdAndValidRequesterId_shouldReturn200() throws Exception {
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
                                .header("requester-user-id", 2))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void testChangeUserDetails_WithInvalidUserIdAndValidRequesterId_shouldReturn401() throws Exception {
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
                                .header("requester-user-id", 2))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetails_WithInvalidUserIdAndInvalidRequesterId_shouldReturn400() throws Exception {
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
    void testInactivateAccount_WithValidUserIdAndValidRequesterId_shouldReturn204() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/2/inactivate")
                                .header("requester-user-id", 2))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    void testInactivateAccount_WithInvalidUserIdAndValidRequesterId_shouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/192/inactivate")
                                .header("requester-user-id", 2))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testInactivateAccount_WithInvalidUserIdAndInvalidRequesterId_shouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/192/inactivate")
                                .header("Authorization", "sdds"))
                //THEN
                .andExpect(status().isBadRequest());
    }

    private ResultActions registerValidUser() throws Exception {
        return mvc.perform(
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