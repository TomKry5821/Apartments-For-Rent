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
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
    void testGetAllActiveAnnouncements_WithEmptyDatabase() {
        //GIVEN
        Collection<AnnouncementDTO> announcementDTOS = new ArrayList<>();

        //WHEN
        var expected = this.announcementService.getAllActiveAnnouncements();

        //THEN
        assertEquals(expected, announcementDTOS);
    }

    @Test
    void testGetAllActiveAnnouncements_WithNotEmptyDatabase() throws InvalidUserIdException {
        //GIVEN
        var addNewAnnouncementRequest = this.validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());

        //WHEN
        var expected = this.announcementService.getAllActiveAnnouncements();

        //THEN
        assertFalse(expected.isEmpty());
    }

    @Test
    void testAddNewAnnouncement_WithValidRequestBody() throws InvalidUserIdException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();

        //WHEN
        var expected = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        expected.setCreationDate(null);
        expected.setRentalTerm(null);

        //THEN
        assertFalse(this.announcementRepository.findAll().isEmpty());

    }

    @Test
    void testAddNewAnnouncement_WithInvalidRequestBody() {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();

        //WHEN
        addNewAnnouncementRequest.setUserId(100L);

        //THEN
        assertThrows(InvalidUserIdException.class, () ->
                this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, 1L));
    }


    @Test
    void testGetAnnouncementWithAllDetails_WithValidAnnouncementId() throws InvalidUserIdException, AnnouncementNotFoundException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var getAnnouncementDetailsResponse = validGetAnnouncementWithAllDetailsResponse();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());

        //WHEN
        var response = this.announcementService.getAnnouncementWithAllDetails(addNewAnnouncementResponse.getAnnouncementId());
        response.setRentalTerm(null);
        response.setCreationDate(null);
        //THEN
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, getAnnouncementDetailsResponse);
    }

    @Test
    void testGetAnnouncementWithAllDetails_WithInvalidAnnouncementId() throws InvalidUserIdException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.getAnnouncementWithAllDetails(0L));
    }

    @Test
    void testUpdateAnnouncement_WithValidRequestBodyAndUserId() throws InvalidUserIdException, AnnouncementNotFoundException, ClosedAnnouncementException {
        //GIVEN
        var updateAnnouncementRequest = validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        var updateAnnouncementResponse = validUpdateAnnouncementResponse();

        //WHEN
        var expected = this.announcementService.updateAnnouncement(updateAnnouncementRequest, addAnnouncementResponse.getAnnouncementId(), addAnnouncementResponse.getUserId());

        //THEN
        Assertions.assertEquals(expected, updateAnnouncementResponse);
    }

    @Test
    void testUpdateAnnouncement_WithInvalidUserId() throws InvalidUserIdException {
        //GIVEN
        var updateAnnouncementRequest = validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.updateAnnouncement(updateAnnouncementRequest, addNewAnnouncementResponse.getAnnouncementId(), 0L));
    }

    @Test
    void testUpdateAnnouncement_WithClosedAnnouncement() throws InvalidUserIdException, AnnouncementNotFoundException {
        //GIVEN
        var updateAnnouncementRequest = validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());
        this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 1L);

        //WHEN AND THEN
        Assertions.assertThrows(ClosedAnnouncementException.class, () ->
                this.announcementService.updateAnnouncement(updateAnnouncementRequest, addNewAnnouncementResponse.getAnnouncementId(), 1L));
    }

    @Test
    void testCloseAnnouncement_WithValidUserId() throws InvalidUserIdException, AnnouncementNotFoundException {
        //GIVEN
        validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());

        //WHEN
        this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), addNewAnnouncementResponse.getUserId());

        //THEN
        Assertions.assertDoesNotThrow(AnnouncementNotFoundException::new);
    }

    @Test
    void testCloseAnnouncement_WithInvalidUserId() throws InvalidUserIdException {
        //GIVEN
        validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest, addNewAnnouncementRequest.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), 0L));
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

    private AddNewAnnouncementResponse validAnnouncementResponse() {
        return AddNewAnnouncementResponse.builder()
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