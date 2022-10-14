package pl.polsl.krypczyk.apartmentsforrent.userservice.application.security;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void test_LoginUserWithValidCredentialsShouldReturn200() throws Exception {
        //GIVEN
        this.createValidUser();

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
    void testLoginUserWithInvalidCredentialsShouldReturn400() throws Exception {
        //GIVEN
        this.createValidUser();

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
    void testLoginUserWithNullCredentialsShouldReturn400() throws Exception {
        //GIVEN
        this.createValidUser();

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
    void testLogoutUserWithValidIdShouldReturn204() throws Exception {
        //GIVEN
        this.createValidUser();
        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/2/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isNoContent());

    }

    @Test
    void testLogoutUserWithInvalidIdShouldReturn401() throws Exception {
        //GIVEN
        this.createValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/auth/100/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "kjnjghy"))
                //THEN
                .andExpect(status().isUnauthorized());

    }

    private void createValidUser() throws Exception {
        mvc.perform(
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
