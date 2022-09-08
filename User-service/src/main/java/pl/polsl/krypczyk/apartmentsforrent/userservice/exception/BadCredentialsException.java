package pl.polsl.krypczyk.apartmentsforrent.userservice.exception;

public class BadCredentialsException extends Exception {

    private static final String BAD_CREDENTIALS_MESSAGE = "Provided credentials are invalid";

    public BadCredentialsException() {
        super(BAD_CREDENTIALS_MESSAGE);
    }
}
