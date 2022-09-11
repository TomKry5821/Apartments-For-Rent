package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorizationService authorizationService;

    @AfterEach
    void deleteDbContent() {
        this.authorizationService.deleteDbContent();
    }

    @Test
    void registerNewUserWithValidUserDetails_ShouldReturn201() throws Exception {
        //GIVEN AND WHEN
        var result = this.registerValidUser();

        //THEN
        result.andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void registerNewUserWithInvalidUserDetails_ShouldReturn400() throws Exception {
        mvc.perform(
                        post("/user/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"name\": \"\",\n" +
                                        "    \"surname\": \"]\",\n" +
                                        "    \"email\": \"testtest.pl\",\n" +
                                        "    \"isActive\": null,\n" +
                                        "    \"password\": \"Test\"\n" +
                                        "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    void registerExistingUser_ShouldReturn400() throws Exception {
        //GIVEN AND WHEN
        this.registerValidUser();
        var result = this.registerValidUser();
        //THEN
                result.andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    void loginUserWithValidCredentials_ShouldReturn200() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"email\": \"test@test.pl\",\n" +
                                        "    \"password\": \"Test\"\n" +
                                        "}"))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void loginUserWithInvalidCredentials_ShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"email\": \"test@telkst.pl\",\n" +
                                        "    \"password\": \"Testjnj\"\n" +
                                        "}"))
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));

    }

    @Test
    void loginUserWithNullCredentials_ShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"email\": \"\",\n" +
                                        "    \"password\": null\n" +
                                        "}"))
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));

    }

    @Test
    void logoutUserWithValidId_ShouldReturn204() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/6/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "kjnjghy"))
                //THEN
                .andExpect(status().isNoContent());

    }

    @Test
    void logoutUserWithInvalidId_ShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/100/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "kjnjghy"))
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