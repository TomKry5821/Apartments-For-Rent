package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion;

public class ClosedAnnouncementException extends RuntimeException {
    private final static String CLOSED_ANNOUNCEMENT_MESSAGE = "Announcement with provided id is closed";

    public ClosedAnnouncementException() {
        super(CLOSED_ANNOUNCEMENT_MESSAGE);
    }
}
