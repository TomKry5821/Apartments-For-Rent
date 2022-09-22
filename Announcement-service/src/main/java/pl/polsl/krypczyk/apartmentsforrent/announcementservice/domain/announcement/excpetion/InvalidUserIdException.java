package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion;

public class InvalidUserIdException extends Exception {
    private static final String INVALID_USER_ID_MESSAGE = "Provided user is invalid";

    public InvalidUserIdException() {
        super(INVALID_USER_ID_MESSAGE);
    }
}
