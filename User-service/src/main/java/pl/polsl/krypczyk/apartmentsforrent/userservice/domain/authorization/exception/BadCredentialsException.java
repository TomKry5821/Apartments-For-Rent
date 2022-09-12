package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception;

public class BadCredentialsException extends RuntimeException {

    private static final String BAD_CREDENTIALS_MESSAGE = "Provided credentials are invalid";

    public BadCredentialsException() {
        super(BAD_CREDENTIALS_MESSAGE);
    }
}
