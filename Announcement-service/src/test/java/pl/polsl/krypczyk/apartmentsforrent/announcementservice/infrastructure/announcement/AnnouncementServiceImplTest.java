package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.UpdateAnnouncementResponse;

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
    void testGetAllActiveAnnouncements_WithNotEmptyDatabase() {
        //GIVEN
        var request = this.validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(request, request.getUserId());

        //WHEN
        var expected = this.announcementService.getAllActiveAnnouncements();

        //THEN
        assertFalse(expected.isEmpty());
    }

    @Test
    void testAddNewAnnouncement() {
        //GIVEN
        var request = validAnnouncementRequest();
        var response = validAnnouncementResponse();

        //WHEN
        var expected = this.announcementService.addNewAnnouncement(request, request.getUserId());
        expected.setCreationDate(null);
        expected.setRentalTerm(null);

        //THEN
        assertEquals(expected, response);

    }

    @Test
    void testGetAnnouncementWithAllDetails_WithValidAnnouncementId() {
        //GIVEN
        var request = validAnnouncementRequest();
        var getAnnouncementDetailsResponse = validGetAnnouncementWithAllDetailsResponse();
        this.announcementService.addNewAnnouncement(request, request.getUserId());
        var id = this.announcementRepository.findAll().get(0).getId();

        //WHEN
        var response = this.announcementService.getAnnouncementWithAllDetails(id);
        response.setRentalTerm(null);
        response.setCreationDate(null);
        //THEN
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, getAnnouncementDetailsResponse);
    }

    @Test
    void testGetAnnouncementWithAllDetails_WithInvalidAnnouncementId() {
        //GIVEN
        var request = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(request, request.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.getAnnouncementWithAllDetails(0L));
    }

    @Test
    void testUpdateAnnouncement_WithValidRequestBodyAndUserId() {
        //GIVEN
        var request = validUpdateAnnouncementRequest();
        var addRequest = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addRequest, addRequest.getUserId());
        var response = validUpdateAnnouncementResponse();

        //WHEN
        var expected = this.announcementService.updateAnnouncement(request, 4L, 1L);

        //THEN
        Assertions.assertEquals(expected, response);
    }

    @Test
    void testUpdateAnnouncement_WithInvalidUserId() {
        //GIVEN
        var request = validUpdateAnnouncementRequest();
        var addRequest = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addRequest, addRequest.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.updateAnnouncement(request, 1L, 1L));
    }

    private AddNewAnnouncementRequest validAnnouncementRequest() {
        var createAnnouncementRequest = new AddNewAnnouncementRequest();
        createAnnouncementRequest.setCity("City");
        createAnnouncementRequest.setCaution(BigDecimal.valueOf(10.55));
        createAnnouncementRequest.setContent("Content");
        createAnnouncementRequest.setDistrict("District");
        createAnnouncementRequest.setLocalNumber(1);
        createAnnouncementRequest.setBuildingNumber("1A");
        createAnnouncementRequest.setStreet("Street");
        createAnnouncementRequest.setTitle("Title");
        createAnnouncementRequest.setPhotoPaths(List.of("test", "test2"));
        createAnnouncementRequest.setMainPhotoPath("path");
        createAnnouncementRequest.setRentalAmount(BigDecimal.valueOf(10.55));
        createAnnouncementRequest.setRoomsNumber(1);
        createAnnouncementRequest.setZipCode("11-111");
        createAnnouncementRequest.setRentalTerm(LocalDate.now());
        createAnnouncementRequest.setUserId(1L);

        return createAnnouncementRequest;
    }

    private AddNewAnnouncementResponse validAnnouncementResponse() {
        var createAnnouncementResponse = new AddNewAnnouncementResponse();
        createAnnouncementResponse.setCity("City");
        createAnnouncementResponse.setCaution(BigDecimal.valueOf(10.55));
        createAnnouncementResponse.setContent("Content");
        createAnnouncementResponse.setDistrict("District");
        createAnnouncementResponse.setLocalNumber(1);
        createAnnouncementResponse.setBuildingNumber("1A");
        createAnnouncementResponse.setStreet("Street");
        createAnnouncementResponse.setTitle("Title");
        createAnnouncementResponse.setPhotoPaths(List.of("test", "test2"));
        createAnnouncementResponse.setMainPhotoPath("path");
        createAnnouncementResponse.setRentalAmount(BigDecimal.valueOf(10.55));
        createAnnouncementResponse.setRoomsNumber(1);
        createAnnouncementResponse.setZipCode("11-111");
        createAnnouncementResponse.setRentalTerm(null);
        createAnnouncementResponse.setUserId(1L);
        createAnnouncementResponse.setCreationDate(null);
        createAnnouncementResponse.setIsClosed(false);

        return createAnnouncementResponse;
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