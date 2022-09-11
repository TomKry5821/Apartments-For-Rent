package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String USER_NOT_FOUND_MESSAGE = "User could not been found";

    public UserNotFoundException() {
        super(USER_NOT_FOUND_MESSAGE);
    }
}
