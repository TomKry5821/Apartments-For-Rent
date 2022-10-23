package pl.polsl.krypczyk.apartmentsforrent.userservice.application.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.polsl.krypczyk.apartmentsforrent.userservice.infrastructure.security.config.AES;

import java.util.Objects;

class AESTest {

    private static final String ENCRYPTED_PASSWORD = "H6LsgM/nxKwv+1yR3wlLxg==";
    private static final String DECRYPTED_PASSWORD = "Admin";


    @Test
    void testEncryptPasswordWithValidDataShouldReturnEncryptedPassword() {
        //GIVEN
        //WHEN
        var expected = AES.encrypt(DECRYPTED_PASSWORD);

        //THEN
        Assertions.assertEquals(expected, ENCRYPTED_PASSWORD);
    }

    @Test
    void testEncryptPasswordWitInvalidDataShouldReturnNullPassword() {
        //GIVEN
        //WHEN
        var expected = AES.encrypt(null);

        //THEN
        Assertions.assertTrue(Objects.isNull(expected));
    }

    @Test
    void testDecryptPasswordWithValidDataShouldReturnDecryptedPassword() {
        //GIVEN
        //WHEN
        var expected = AES.decrypt(ENCRYPTED_PASSWORD);

        //THEN
        Assertions.assertEquals(expected, DECRYPTED_PASSWORD);
    }

    @Test
    void testDecryptPasswordWithInvalidDataShouldReturnNullPassword() {
        //GIVEN
        //WHEN
        var expected = AES.encrypt(null);

        //THEN
        Assertions.assertTrue(Objects.isNull(expected));
    }
}