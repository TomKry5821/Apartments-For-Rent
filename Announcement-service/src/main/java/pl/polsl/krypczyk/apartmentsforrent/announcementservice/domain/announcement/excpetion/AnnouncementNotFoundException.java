package pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion;

public class AnnouncementNotFoundException extends RuntimeException {
    private final static String ANNOUNCEMENT_NOT_FOUND_MESSAGE = "Announcement with provided id could not been found";

    public AnnouncementNotFoundException() {
        super(ANNOUNCEMENT_NOT_FOUND_MESSAGE);
    }
}
