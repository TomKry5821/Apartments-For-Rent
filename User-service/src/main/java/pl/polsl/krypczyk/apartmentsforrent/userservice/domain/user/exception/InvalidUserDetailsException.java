package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception;


public class InvalidUserDetailsException extends RuntimeException  {
    private static final String INVALID_USER_DETAILS_MESSAGE = "Provided user details are invalid";

    public InvalidUserDetailsException(){
        super(INVALID_USER_DETAILS_MESSAGE);
    }
}
