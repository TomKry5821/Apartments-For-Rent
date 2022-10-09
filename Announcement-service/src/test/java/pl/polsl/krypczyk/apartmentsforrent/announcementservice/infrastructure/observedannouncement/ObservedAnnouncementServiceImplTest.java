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
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class ObservedAnnouncementServiceImplTest {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ObservedAnnouncementRepository observedAnnouncementRepository;

    @BeforeEach
    void deleteDbContent() {
        this.announcementRepository.deleteAll();
    }

    @Test
    void testObserveAnnouncement_WithValidUserIdAndValidAnnouncementId() throws InvalidUserIdException, AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        var expected = this.validObserveAnnouncementResponse(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //WHEN
        var actual = this.announcementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());
        //THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testObserveAnnouncement_WithInvalidUserIdAndValidAnnouncementId() throws InvalidUserIdException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserIdException.class, () ->
                this.announcementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 0L, 1L));
    }

    @Test
    void testObserveAnnouncement_WithValidUserIdAndInvalidAnnouncementId() throws InvalidUserIdException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.observeAnnouncement(0L, addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testObserveAnnouncement_WithAlreadyObservedAnnouncement() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        this.announcementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementAlreadyObservedException.class, () ->
                this.announcementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testObserveAnnouncement_WithClosedAnnouncement() throws InvalidUserIdException, AnnouncementNotFoundException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(ClosedAnnouncementException.class, () ->
                this.announcementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testUnobserveAnnouncement_WithValidUserIdAndValidAnnouncementId() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        this.announcementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //WHEN
        this.announcementService.unobserveAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //THEN
        Assertions.assertDoesNotThrow(InvalidUserIdException::new);
        Assertions.assertDoesNotThrow(AnnouncementNotFoundException::new);
    }

    @Test
    void testUnobserveAnnouncement_WithInvalidUserIdAndValidAnnouncementId() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        this.announcementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserIdException.class, () ->
                this.announcementService.unobserveAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 0L, addNewAnnouncementResponse.getUserId()));
    }

    @Test
    void testUnobserveAnnouncement_WithValidUserIdAndInvalidAnnouncementId() throws InvalidUserIdException, AnnouncementAlreadyObservedException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        this.announcementService.observeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.unobserveAnnouncement(0L, addNewAnnouncementResponse.getUserId(), addNewAnnouncementResponse.getUserId()));
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