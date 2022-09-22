package pl.polsl.krypczyk.apartmentsforrent.gateway.filter;

import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.Objects;

public class TokenUtils {
    private static final String UUID_REGEX_PATTERN = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";

    public static Boolean isTokenCorrect(String token) {
        return
                !Objects.isNull(token)
                        && token.length() == 36
                        && token.matches(UUID_REGEX_PATTERN);
    }
}
