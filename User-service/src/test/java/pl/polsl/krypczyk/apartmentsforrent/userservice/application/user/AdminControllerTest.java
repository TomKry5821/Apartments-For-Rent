package pl.polsl.krypczyk.apartmentsforrent.userservice.application.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.AuthorizationService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AuthorizationService authorizationService;

    @AfterEach
    void deleteDbContent() {
        this.authorizationService.deleteDbContent();
    }

    @Test
    void deleteUserWithInvalidUserIdAndValidToken_shouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();
        var token = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

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