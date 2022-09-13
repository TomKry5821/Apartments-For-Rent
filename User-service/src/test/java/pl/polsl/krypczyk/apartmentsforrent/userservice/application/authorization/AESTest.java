package pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.support.MetaDataAccessException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {

    @Test
    void encryptPasswordWithValidData() {
        //GIVEN
        var encryptedPassword = "H6LsgM/nxKwv+1yR3wlLxg==";
        var password = "Admin";

        //WHEN
        var expected = AES.encrypt(password);

        //THEN
        Assertions.assertTrue(expected.equals(encryptedPassword));
    }

    @Test
    void encryptPasswordWitInvalidData() {
        //GIVEN
        String password = null;

        //WHEN
        var expected = AES.encrypt(password);

        //THEN
        Assertions.assertTrue(Objects.isNull(expected));
    }

    @Test
    void decryptPasswordWithValidData() {
        //GIVEN
        var encryptedPassword = "H6LsgM/nxKwv+1yR3wlLxg==";
        var password = "Admin";

        //WHEN
        var expected = AES.decrypt(encryptedPassword);

        //THEN
        Assertions.assertTrue(expected.equals(password));
    }

    @Test
    void decryptPasswordWithInvalidData() {
        //GIVEN
        String password = null;

        //WHEN
        var expected = AES.encrypt(password);

        //THEN
        Assertions.assertTrue(Objects.isNull(expected));
    }
}