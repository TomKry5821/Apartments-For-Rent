package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception;

public class UnauthorizedUserException extends RuntimeException{
    private static final String UNAUTHORIZED_USER_MESSAGE = "Invalid access token";

    public UnauthorizedUserException(){
        super(UNAUTHORIZED_USER_MESSAGE);
    }
}
