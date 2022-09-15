package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.CreateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.CreateAnnouncementResponse;

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
    void deleteDbContent(){
        this.announcementRepository.deleteAll();
    }

    @Test
    void getAllAnnouncementsWithEmptyDatabase() {
        //GIVEN
        Collection<AnnouncementDTO> announcementDTOS = new ArrayList<>();

        //WHEN
        var expected = this.announcementService.getAllAnnouncements();

        //THEN
        Assertions.assertTrue(expected.equals(announcementDTOS));
    }

    @Test
    void getAllAnnouncementsWithNotEmptyDatabase() {
        //GIVEN
        this.announcementService.createAnnouncement(this.validAnnouncementRequest());

        //WHEN
        var expected = this.announcementService.getAllAnnouncements();

        //THEN
        assertFalse(expected.isEmpty());
    }

    @Test
    void createAnnouncement() {
        //GIVEN
        var request = validAnnouncementRequest();
        var response = validAnnouncementResponse();

        //WHEN
        var expected = this.announcementService.createAnnouncement(request);
        expected.setCreationDate(null);
        expected.setRentalTerm(null);

        //THEN
        assertEquals(expected, response);

    }

    private CreateAnnouncementRequest validAnnouncementRequest(){
        var createAnnouncementRequest = new CreateAnnouncementRequest();
        createAnnouncementRequest.setCity("City");
        createAnnouncementRequest.setCaution(BigDecimal.TEN);
        createAnnouncementRequest.setContent("Content");
        createAnnouncementRequest.setDistrict("District");
        createAnnouncementRequest.setLocalNumber(1);
        createAnnouncementRequest.setBuildingNumber("1A");
        createAnnouncementRequest.setStreet("Street");
        createAnnouncementRequest.setTitle("Title");
        createAnnouncementRequest.setPhotoPaths(List.of("test", "test2"));
        createAnnouncementRequest.setMainPhotoPath("path");
        createAnnouncementRequest.setRentalAmount(BigDecimal.TEN);
        createAnnouncementRequest.setRoomsNumber(1);
        createAnnouncementRequest.setZipCode("11-111");
        createAnnouncementRequest.setRentalTerm(LocalDate.now());
        createAnnouncementRequest.setUserId(1L);

        return createAnnouncementRequest;
    }

    private CreateAnnouncementResponse validAnnouncementResponse(){
        var createAnnouncementResponse = new CreateAnnouncementResponse();
        createAnnouncementResponse.setCity("City");
        createAnnouncementResponse.setCaution(BigDecimal.TEN);
        createAnnouncementResponse.setContent("Content");
        createAnnouncementResponse.setDistrict("District");
        createAnnouncementResponse.setLocalNumber(1);
        createAnnouncementResponse.setBuildingNumber("1A");
        createAnnouncementResponse.setStreet("Street");
        createAnnouncementResponse.setTitle("Title");
        createAnnouncementResponse.setPhotoPaths(List.of("test", "test2"));
        createAnnouncementResponse.setMainPhotoPath("path");
        createAnnouncementResponse.setRentalAmount(BigDecimal.TEN);
        createAnnouncementResponse.setRoomsNumber(1);
        createAnnouncementResponse.setZipCode("11-111");
        createAnnouncementResponse.setRentalTerm(null);
        createAnnouncementResponse.setUserId(1L);
        createAnnouncementResponse.setCreationDate(null);

        return createAnnouncementResponse;
    }

}