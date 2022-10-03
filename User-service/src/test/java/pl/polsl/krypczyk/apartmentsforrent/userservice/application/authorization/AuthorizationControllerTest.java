package pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testRegisterNewUser_WithValidUserDetails_ShouldReturn201() throws Exception {
        //GIVEN AND WHEN
        var result = this.registerValidUser();

        //THEN
        result.andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testRegisterNewUser_WithInvalidUserDetails_ShouldReturn400() throws Exception {
        mvc.perform(
                        post("/user/api/v1/auth/register")
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
    void testRegister_ExistingUser_ShouldReturn400() throws Exception {
        //GIVEN AND WHEN
        this.registerValidUser();
        var result = this.registerValidUser();
        //THEN
        result.andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    void test_LoginUser_WithValidCredentials_ShouldReturn200() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "test@test.pl",
                                            "password": "Test"
                                        }"""))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void testLoginUser_WithInvalidCredentials_ShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "test@telkst.pl",
                                            "password": "Testjnj"
                                        }"""))
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));

    }

    @Test
    void testLoginUser_WithNullCredentials_ShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "",
                                            "password": null
                                        }"""))
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN));

    }

    @Test
    void testLogoutUser_WithValidId_ShouldReturn204() throws Exception {
        //GIVEN
        this.registerValidUser();
        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/2/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("requester-user-id", 2))
                //THEN
                .andExpect(status().isNoContent());

    }

    @Test
    void testLogoutUser_WithInvalidId_ShouldReturn400() throws Exception {
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
