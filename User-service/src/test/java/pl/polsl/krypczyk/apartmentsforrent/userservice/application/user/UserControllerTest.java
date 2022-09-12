package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorizationService authorizationService;

    @AfterEach
    void deleteDbContent() {
        this.authorizationService.deleteDbContent();
    }

    @Test
    void getUserDetailsWithValidUserIdAndToken_shouldReturn200() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/2/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void getUserDetailsWithInvalidUserIdAndValidToken_shouldReturn401() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/100/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserDetailsWithInvalidUserIdAndInvalidToken_shouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/10/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "sddsdd"))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeUserDetailsWithValidUserIdAndValidToken_shouldReturn200() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        put("/user/api/v1/users/2/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Test",
                                            "surname": "Testowy",
                                            "email": "test@test.pl",
                                            "password": "Test"
                                        }""")
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void changeUserDetailsWithInvalidUserIdAndValidToken_shouldReturn401() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

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
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changeUserDetailsWithInvalidUserIdAndInvalidToken_shouldReturn400() throws Exception {
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
    void inactivateAccountWithValidUserIdAndValidToken_shouldReturn204() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/2/inactivate")
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    void inactivateAccountWithInvalidUserIdAndValidToken_shouldReturn401() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/192/inactivate")
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void inactivateAccountWithInvalidUserIdAndInvalidToken_shouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/192/inactivate")
                                .header("Authorization", "sdds"))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUserWithValidUserIdAndValidToken_shouldReturn204() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        delete("/user/api/v1/admin/users/2")
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserWithInvalidUserIdAndValidToken_shouldReturn401() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        delete("/user/api/v1/admin/users/194")
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUserWithInvalidUserIdAndInvalidToken_shouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        delete("/user/api/v1/admin/users/194")
                                .header("Authorization", "sdsd"))
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

    private UUID getTokenFromResponse(ResultActions resultActions) throws UnsupportedEncodingException {
        var token = resultActions.andReturn().getResponse().getContentAsString().lines().toArray()[0].toString();
        token = token.substring(39, 75);
        return UUID.fromString(token);
    }
}