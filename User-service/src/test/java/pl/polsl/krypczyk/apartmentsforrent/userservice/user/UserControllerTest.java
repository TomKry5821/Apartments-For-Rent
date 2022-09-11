package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.polsl.krypczyk.apartmentsforrent.userservice.authorization.AuthorizationService;

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
    void getUserDetailsWithValidUserIdAndToken() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        get("/user/api/v1/users/1/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void getUserDetailsWithInvalidUserIdAndValidToken() throws Exception {
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
    void getUserDetailsWithInvalidUserIdAndInvalidToken() throws Exception {
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
    void changeUserDetailsWithValidUserIdAndValidToken() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        put("/user/api/v1/users/1/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"name\": \"Test\",\n" +
                                        "    \"surname\": \"Testowy\",\n" +
                                        "    \"email\": \"test@test.pl\",\n" +
                                        "    \"password\": \"Test\"\n" +
                                        "}")
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    void changeUserDetailsWithInvalidUserIdAndValidToken() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        put("/user/api/v1/users/10/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"name\": \"Test\",\n" +
                                        "    \"surname\": \"Testowy\",\n" +
                                        "    \"email\": \"test@test.pl\",\n" +
                                        "    \"password\": \"Test\"\n" +
                                        "}")
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void changeUserDetailsWithInvalidUserIdAndInvalidToken() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        put("/user/api/v1/users/10/details")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"name\": \"Test\",\n" +
                                        "    \"surname\": \"Testowy\",\n" +
                                        "    \"email\": \"test@test.pl\",\n" +
                                        "    \"password\": \"Test\"\n" +
                                        "}")
                                .header("Authorization", "sfsf"))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void inactivateAccountWithValidUserIdAndValidToken() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

        //WHEN
        mvc.perform(
                        post("/user/api/v1/users/1/inactivate")
                                .header("Authorization", token))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    void inactivateAccountWithInvalidUserIdAndValidToken() throws Exception {
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
    void inactivateAccountWithInvalidUserIdAndInvalidToken() throws Exception {
        //GIVEN
        var response = this.registerValidUser();
        var token = this.getTokenFromResponse(response);

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
                        .content("{\n" +
                                "    \"name\": \"Test\",\n" +
                                "    \"surname\": \"Testowy\",\n" +
                                "    \"email\": \"test@test.pl\",\n" +
                                "    \"isActive\": true,\n" +
                                "    \"password\": \"Test\"\n" +
                                "}"));
    }

    private UUID getTokenFromResponse(ResultActions resultActions) throws UnsupportedEncodingException {
        var token = resultActions.andReturn().getResponse().getContentAsString().lines().toArray()[0].toString();
        token = token.substring(39, 75);
        return UUID.fromString(token);
    }
}