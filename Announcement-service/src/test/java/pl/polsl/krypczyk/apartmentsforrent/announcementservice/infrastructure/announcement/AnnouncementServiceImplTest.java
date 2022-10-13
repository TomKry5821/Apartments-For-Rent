package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class AnnouncementServiceImplTest {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @BeforeEach
    void deleteDbContent() {
        this.announcementRepository.deleteAll();
    }

    @Test
    void testGetAllActiveAnnouncementsWithEmptyDatabaseShouldReturnExpectedResponse() {
        //GIVEN
        Collection<AnnouncementDTO> announcementDTOS = new ArrayList<>();

        //WHEN
        var expected = this.announcementService.getAllActiveAnnouncements();

        //THEN
        assertEquals(expected, announcementDTOS);
    }

    @Test
    void testGetAllActiveAnnouncementsWithNotEmptyDatabaseShouldReturnNotEmptyResponse() {
        //GIVEN
        var addNewAnnouncementRequest = this.validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        var expected = this.announcementService.getAllActiveAnnouncements();

        //THEN
        assertFalse(expected.isEmpty());
    }

    @Test
    void testAddNewAnnouncementWithValidRequestBodyShouldReturnNotEmptyResponse() {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();

        //WHEN
        var expected = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        expected.setCreationDate(null);
        expected.setRentalTerm(null);

        //THEN
        assertFalse(this.announcementRepository.findAll().isEmpty());

    }

    @Test
    void testGetAnnouncementWithAllDetailsWithValidAnnouncementIdShouldReturnExpectedResponse() throws AnnouncementNotFoundException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var getAnnouncementDetailsResponse = validGetAnnouncementWithAllDetailsResponse();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        var response = this.announcementService.getAnnouncementWithAllDetails(addNewAnnouncementResponse.getAnnouncementId());
        response.setRentalTerm(null);
        response.setCreationDate(null);

        //THEN
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, getAnnouncementDetailsResponse);
    }

    @Test
    void testGetAnnouncementWithAllDetailsWithInvalidAnnouncementIdShouldThrowAnnouncementNotFoundException() {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.getAnnouncementWithAllDetails(0L));
    }

    @Test
    void testUpdateAnnouncementWithValidRequestBodyAndUserIdShouldReturnExpectedResponse() throws AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var updateAnnouncementRequest = validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        var updateAnnouncementResponse = validUpdateAnnouncementResponse();

        //WHEN
        var expected = this.announcementService.updateAnnouncement(updateAnnouncementRequest, addAnnouncementResponse.getAnnouncementId());

        //THEN
        Assertions.assertEquals(expected, updateAnnouncementResponse);
    }

    @Test
    void testUpdateAnnouncementWithClosedAnnouncementShouldThrowClosedAnnouncementException() throws InvalidUserIdException, AnnouncementNotFoundException {
        //GIVEN
        var updateAnnouncementRequest = validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 1L);

        //WHEN AND THEN
        Assertions.assertThrows(ClosedAnnouncementException.class, () ->
                this.announcementService.updateAnnouncement(updateAnnouncementRequest, addNewAnnouncementResponse.getAnnouncementId()));
    }

    @Test
    void testCloseAnnouncementWithValidUserIdShouldNotThrowAnnouncementNotFoundException() throws InvalidUserIdException, AnnouncementNotFoundException {
        //GIVEN
        validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //THEN
        Assertions.assertDoesNotThrow(AnnouncementNotFoundException::new);
    }

    @Test
    void testCloseAnnouncementWithInvalidUserIdShouldThrowInvalidUserIdException() {
        //GIVEN
        validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN AND THEN
        Assertions.assertThrows(InvalidUserIdException.class, () ->
                this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 0L));
    }

    @Test
    void testCloseUserAnnouncementsListenerWithValidUserIdShouldReturnEmptyActiveAnnouncementsList() {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(announcement);

        //WHEN
        this.announcementService.closeUserAnnouncements(announcement.getUserId());

        //THEN
        Assertions.assertTrue(this.announcementRepository.findAnnouncementEntitiesByIsClosed(false).isEmpty());
    }

    @Test
    void deleteUserAnnouncementsListenerWithValidUserIdShouldReturnEmptyAnnouncementsList() {
        //GIVEN
        var announcement = this.validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(announcement);

        //WHEN
        this.announcementService.deleteUserAnnouncements(announcement.getUserId());

        //THEN
        Assertions.assertTrue(this.announcementRepository.findAll().isEmpty());
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

    private GetAnnouncementWithAllDetailsResponse validGetAnnouncementWithAllDetailsResponse() {
        return GetAnnouncementWithAllDetailsResponse
                .builder()
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
                .rentalTerm(null)
                .userId(1L)
                .creationDate(null)
                .isClosed(false)
                .build();
    }

    private UpdateAnnouncementRequest validUpdateAnnouncementRequest() {
        return UpdateAnnouncementRequest
                .builder()
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
                .rentalTerm(null)
                .build();
    }

    private UpdateAnnouncementResponse validUpdateAnnouncementResponse() {
        return UpdateAnnouncementResponse
                .builder()
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
                .rentalTerm(null)
                .build();
    }

}