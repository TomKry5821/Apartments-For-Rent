package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.observedannouncement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@Transactional
class ObservedAnnouncementServiceImplTest {

    private static final String CITY = "City";
    private static final BigDecimal CAUTION = BigDecimal.valueOf(10.55);
    private static final String CONTENT = "Content";
    private static final String DISTRICT = "District";
    private static final int LOCAL_NUMBER = 1;
    private static final String BUILDING_NUMBER = "1A";
    private static final String STREET = "Street";
    private static final String TITLE = "Title";
    private static final List<MultipartFile> PHOTO_FILES = List.of(
            new MockMultipartFile("test1", new byte[5]),
            new MockMultipartFile("test2", new byte[5]));
    private static final MultipartFile MAIN_PHOTO_FILE = new MockMultipartFile("test", new byte[5]);
    private static final BigDecimal RENTAL_AMOUNT = BigDecimal.valueOf(10.55);
    private static final int ROOMS_NUMBER = 1;
    private static final String ZIP_CODE = "11-111";
    private static final LocalDate RENTAL_TERM = LocalDate.now();
    private static final long USER_ID = 1L;
    private static final long ANNOUNCEMENT_ID = 0L;
    @Autowired
    private ObservedAnnouncementService observedAnnouncementService;

    @Autowired
    private ObservedAnnouncementRepository observedAnnouncementRepository;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @BeforeEach
    void deleteDbContent() {
        this.announcementRepository.deleteAll();
    }

    @Test
    void testObserveAnnouncementWithValidUserIdAndValidAnnouncementIdShouldReturnNotEmptyObservedAnnouncementsList() throws AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //THEN
        Assertions.assertFalse(this.observedAnnouncementRepository.findAll().isEmpty());
    }

    @Test
    void testObserveAnnouncementWithValidUserIdAndInvalidAnnouncementIdShouldThrowAnnouncementNotFoundException() {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        //THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.observedAnnouncementService.observeAnnouncement(ANNOUNCEMENT_ID, addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testObserveAnnouncementWithAlreadyObservedAnnouncementShouldThrowAnnouncementAlreadyObservedException() throws AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //WHEN
        //THEN
        Assertions.assertThrows(AnnouncementAlreadyObservedException.class, () ->
                this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testObserveAnnouncementWithClosedAnnouncementShouldThrowClosedAnnouncementException() throws AnnouncementNotFoundException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //WHEN
        //THEN
        Assertions.assertThrows(ClosedAnnouncementException.class, () ->
                this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testUnobserveAnnouncementWithValidUserIdAndValidAnnouncementIdShouldNotThrowInvalidUserAndAnnouncementNotFoundException() throws AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //WHEN
        this.observedAnnouncementService.unobserveAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //THEN
        Assertions.assertDoesNotThrow(AnnouncementNotFoundException::new);
    }

    @Test
    void testUnobserveAnnouncementWithValidUserIdAndInvalidAnnouncementIdShouldThrowAnnouncementNotFoundException() throws AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //WHEN
        //THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.observedAnnouncementService.unobserveAnnouncement(ANNOUNCEMENT_ID, addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void deleteUserObservedAnnouncementsListenerWithValidUserIdShouldReturnEmptyObservedAnnouncementsList() throws AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(announcement);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), USER_ID);

        //WHEN
        this.observedAnnouncementService.deleteObservedAnnouncements(announcement.getUserId());

        //THEN
        Assertions.assertTrue(this.observedAnnouncementRepository.findAll().isEmpty());
    }

    @Test
    void getObservedAnnouncementWithValidUserIdShouldReturnNotEmptyResult() throws AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(announcement);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), USER_ID);

        //WHEN
        var result = this.observedAnnouncementService.getObservedAnnouncements(USER_ID);

        //THEN
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void getObservedAnnouncementWithValidUserIdAndEmptyObservedAnnouncementsShouldReturnEmptyResult() {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(announcement);

        //WHEN
        var result = this.observedAnnouncementService.getObservedAnnouncements(USER_ID);

        //THEN
        Assertions.assertTrue(result.isEmpty());
    }

    private AddNewAnnouncementRequest validAnnouncementRequest() {
        return AddNewAnnouncementRequest.builder()
                .city(CITY)
                .caution(CAUTION)
                .content(CONTENT)
                .district(DISTRICT)
                .localNumber(LOCAL_NUMBER)
                .buildingNumber(BUILDING_NUMBER)
                .street(STREET)
                .title(TITLE)
                .photos(PHOTO_FILES)
                .mainPhoto(MAIN_PHOTO_FILE)
                .rentalAmount(RENTAL_AMOUNT)
                .roomsNumber(ROOMS_NUMBER)
                .zipCode(ZIP_CODE)
                .rentalTerm(RENTAL_TERM)
                .userId(USER_ID)
                .build();
    }

}