package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception;

public class UserAlreadyExistsException extends Exception {
    private static final String USER_ALREADY_EXISTS_MESSAGE = "User with provided e-mail already exists";

    public UserAlreadyExistsException() {
        super(USER_ALREADY_EXISTS_MESSAGE);
    }
}
