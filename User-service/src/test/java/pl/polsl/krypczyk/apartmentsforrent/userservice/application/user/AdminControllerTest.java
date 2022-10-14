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
    void testDeleteUserWithInvalidUserIdShouldReturn400() throws Exception {
        //GIVEN
        this.registerValidUser();
        //WHEN
        mvc.perform(
                        delete("/user/api/v1/admin/users/194")
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUserWithInvalidUserIdAndInvalidRolesShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        delete("/user/api/v1/admin/users/194")
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllUsersWithInvalidUserIdAndValidRolesShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();
        //WHEN
        mvc.perform(
                        get("/user/api/v1/admin/users")
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllUsersWithInvalidUserIdAndInvalidRolesShouldReturn401() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        get("/user/api/v1/admin/users")
                                .header("X-USER-ID", 211)
                                .header("X-USER-ROLES", "[ROLE_USER]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdAndValidRolesShouldReturn401() throws Exception {
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
                                .header("X-USER-ID", 2L)
                                .header("X-USER-ROLES", "[ROLE_USER]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangeUserDetailsWithInvalidUserIdAndInvalidRolesShouldReturn401() throws Exception {
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
                                .header("Authorization", "sfsf")
                                .header("X-USER-ID", 2)
                                .header("X-USER-ROLES", "[ROLE_USER]"))
                //THEN
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testActivateAccountWithValidUserIdAndValidRequesterIdShouldReturn204() throws Exception {
        //GIVEN
        this.registerValidUser();

        //WHEN
        mvc.perform(
                        post("/user/api/v1/admin/users/1/activate")
                                .header("Authorization", "1")
                                .header("X-USER-ID", 1)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
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
                                .header("X-USER-ID", 1L)
                                .header("X-USER-ROLES", "[ROLE_USER, ROLE_ADMIN]"))
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