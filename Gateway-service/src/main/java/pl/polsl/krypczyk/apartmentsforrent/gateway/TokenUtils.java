package pl.polsl.krypczyk.apartmentsforrent.gateway;

import java.util.Objects;

public class TokenUtils {

    public static Boolean isTokenCorrect(String token) {
        return
                !Objects.isNull(token) &&
                        token.length() == 36;
    }
}
