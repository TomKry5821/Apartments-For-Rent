package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.observedannouncement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class ObservedAnnouncementServiceImplTest {

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
    void testObserveAnnouncementWithValidUserIdAndValidAnnouncementIdShouldReturnExpectedResponse() throws InvalidUserIdException, AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        var expected = this.validObserveAnnouncementResponse(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //WHEN
        var actual = this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());
        //THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testObserveAnnouncementWithInvalidUserIdAndValidAnnouncementIdShouldThrowInvalidUserIdException() {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserIdException.class, () ->
                this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 0L, 1L));
    }

    @Test
    void testObserveAnnouncementWithValidUserIdAndInvalidAnnouncementIdShouldThrowAnnouncementNotFoundException()   {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.observedAnnouncementService.observeAnnouncement(0L, addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testObserveAnnouncementWithAlreadyObservedAnnouncementShouldThrowAnnouncementAlreadyObservedException() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementAlreadyObservedException.class, () ->
                this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testObserveAnnouncementWithClosedAnnouncementShouldThrowClosedAnnouncementException() throws InvalidUserIdException, AnnouncementNotFoundException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(ClosedAnnouncementException.class, () ->
                this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testUnobserveAnnouncementWithValidUserIdAndValidAnnouncementIdShouldNotThrowInvalidUserAndAnnouncementNotFoundException() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //WHEN
        this.observedAnnouncementService.unobserveAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //THEN
        Assertions.assertDoesNotThrow(InvalidUserIdException::new);
        Assertions.assertDoesNotThrow(AnnouncementNotFoundException::new);
    }

    @Test
    void testUnobserveAnnouncementWithInvalidUserIdAndValidAnnouncementIdShouldThrowInvalidUserIdException() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserIdException.class, () ->
                this.observedAnnouncementService.unobserveAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 0L, addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testUnobserveAnnouncementWithValidUserIdAndInvalidAnnouncementIdShouldThrowAnnouncementNotFoundException() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.observedAnnouncementService.unobserveAnnouncement(0L, addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void deleteUserObservedAnnouncementsListenerWithValidUserIdShouldReturnEmptyObservedAnnouncementsList() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(announcement);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 1L, 1L);

        //WHEN
        this.observedAnnouncementService.deleteObservedAnnouncements(announcement.getUserId());

        //THEN
        Assertions.assertTrue(this.observedAnnouncementRepository.findAll().isEmpty());
    }

    @Test
    void getObservedAnnouncementWithValidUserIdShouldReturnNotEmptyResult() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(announcement);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 1L, 1L);

        //WHEN
        var result = this.observedAnnouncementService.getObservedAnnouncements(1L, 1L);

        //THEN
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void getObservedAnnouncementWithValidUserIdAndEmptyObservedAnnouncementsShouldReturnEmptyResult() throws InvalidUserIdException {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(announcement);

        //WHEN
        var result = this.observedAnnouncementService.getObservedAnnouncements(1L, 1L);

        //THEN
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getObservedAnnouncementWithInvalidUserIdShouldThrowInvalidUserException() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(announcement);
        this.observedAnnouncementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 1L, 1L);

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserIdException.class, () ->
                this.observedAnnouncementService.getObservedAnnouncements(0L, 1L));
    }

    private AddNewAnnouncementRequest validAnnouncementRequest() {
        return AddNewAnnouncementRequest.builder()
                .city("City")
                .caution(BigDecimal.valueOf(10.55))
                .content("Content")
                .district("District")
                .localNumber(1)
                .buildingNumber("1A")
                .street("Street")
                .title("Title")
                .photoPaths(List.of("test", "test2"))
                .mainPhotoPath("path")
                .rentalAmount(BigDecimal.valueOf(10.55))
                .roomsNumber(1)
                .zipCode("11-111")
                .rentalTerm(LocalDate.now())
                .userId(1L)
                .build();
    }

    private ObserveAnnouncementResponse validObserveAnnouncementResponse(Long announcementId, Long userId) {
        return ObserveAnnouncementResponse
                .builder()
                .announcementId(announcementId)
                .userId(userId)
                .build();
    }

}