package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testCreateUserWithValidUserDetailsShouldReturn201() throws Exception {
        //GIVEN AND WHEN
        var result = this.createValidUser();

        //THEN
        result.andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateUserWithInvalidUserDetailsShouldReturn400() throws Exception {
        mvc.perform(
                        post("/user/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "",
                                            "surname": "]",
                                            "email": "testtest.pl",
                                            "isActive": null,
                                            "password": "Test"
                                        }"""))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    void testCreaterExistingUserShouldReturn400() throws Exception {
        //GIVEN AND WHEN
        this.createValidUser();
        var result = this.createValidUser();
        //THEN
        result.andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    void testGetUserDetailsWithValidUserIdAndRequesterIdShouldReturn200() throws Exception {
        //GIVEN
        this.createValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/2/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserDetailsWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.createValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/100/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetailsWithValidUserIdAndValidRequesterIdShouldReturn200() throws Exception {
        //GIVEN
        this.createValidUser();

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
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.createValidUser();

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
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdAndInvalidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.createValidUser();

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
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testInactivateAccountWithValidUserIdAndValidRequesterIdShouldReturn204() throws Exception {
        //GIVEN
        this.createValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/2/inactivate")
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    void testInactivateAccountWithInvalidUserIdAndValidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.createValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/192/inactivate")
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testInactivateAccountWithInvalidUserIdAndInvalidRequesterIdShouldReturn401() throws Exception {
        //GIVEN
        this.createValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/192/inactivate")
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    private ResultActions createValidUser() throws Exception {
        return mvc.perform(
                post("/user/api/v1/users")
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