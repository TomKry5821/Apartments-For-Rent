package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception;

public class AnnouncementAlreadyObservedException extends Exception {
    private static final String ANNOUNCEMENT_ALREADY_OBSERVED_MESSAGE = "User with provided id has already observed announcement with provided id";

    public AnnouncementAlreadyObservedException() {
        super(ANNOUNCEMENT_ALREADY_OBSERVED_MESSAGE);
    }
}
