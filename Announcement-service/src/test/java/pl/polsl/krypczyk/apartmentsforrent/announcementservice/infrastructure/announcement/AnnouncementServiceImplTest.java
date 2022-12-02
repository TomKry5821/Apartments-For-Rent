package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@Transactional
class AnnouncementServiceImplTest {

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
    private static final long INVALID_ANNOUNCEMENT_ID = 0L;
    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementDetailsRepository announcementDetailsRepository;

    @Autowired
    private AddressDetailsRepository addressDetailsRepository;

    @Autowired
    private AnnouncementContentRepository announcementContentRepository;

    @BeforeEach
    void deleteDbContent() {
        this.announcementRepository.deleteAll();
    }

    @Test
    void testGetAllActiveAnnouncementsWithEmptyDatabaseShouldReturnExpectedResponse() {
        //GIVEN
        Collection<AnnouncementDTO> expected = new ArrayList<>();

        //WHEN
        var actual = this.announcementService.getAllActiveAnnouncements();

        //THEN
        assertEquals(expected, actual);
    }

    @Test
    void testGetAllActiveAnnouncementsWithNotEmptyDatabaseShouldReturnNotEmptyResponse() {
        //GIVEN
        var addNewAnnouncementRequest = this.validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        var actual = this.announcementService.getAllActiveAnnouncements();

        //THEN
        assertFalse(actual.isEmpty());
    }

    @Test
    void testAddNewAnnouncementWithValidRequestBodyShouldReturnNotEmptyResponse() {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();

        //WHEN
        this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //THEN
        assertFalse(this.announcementRepository.findAll().isEmpty());
        assertFalse(this.announcementDetailsRepository.findAll().isEmpty());
        assertFalse(this.announcementContentRepository.findAll().isEmpty());
        assertFalse(this.addressDetailsRepository.findAll().isEmpty());
        assertFalse(this.announcementContentRepository.findAll().get(0).getPhotos().isEmpty());

    }

    @Test
    void testGetAnnouncementWithAllDetailsWithValidAnnouncementIdShouldReturnNotNullResponse() throws AnnouncementNotFoundException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        var actual = this.announcementService.getAnnouncementWithAllDetails(addNewAnnouncementResponse.getAnnouncementId());

        //THEN
        Assertions.assertNotNull(actual);
    }

    @Test
    void testGetAnnouncementWithAllDetailsWithInvalidAnnouncementIdShouldThrowAnnouncementNotFoundException() {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.getAnnouncementWithAllDetails(INVALID_ANNOUNCEMENT_ID));
    }

    @Test
    void testUpdateAnnouncementWithValidRequestBodyAndUserIdShouldNotThrowAnnouncementNotFoundAndClosedAnnouncementException() throws AnnouncementNotFoundException, ClosedAnnouncementException, IOException {
        //GIVEN
        var updateAnnouncementRequest = validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        this.announcementService.updateAnnouncement(updateAnnouncementRequest, addAnnouncementResponse.getAnnouncementId());

        //THEN
        Assertions.assertDoesNotThrow(AnnouncementNotFoundException::new);
        Assertions.assertDoesNotThrow(ClosedAnnouncementException::new);
    }

    @Test
    void testUpdateAnnouncementWithClosedAnnouncementShouldThrowClosedAnnouncementException() throws AnnouncementNotFoundException {
        //GIVEN
        var updateAnnouncementRequest = validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);
        this.announcementService.closeAnnouncement(addNewAnnouncementResponse.getAnnouncementId(), USER_ID);

        //WHEN
        //THEN
        Assertions.assertThrows(ClosedAnnouncementException.class, () ->
                this.announcementService.updateAnnouncement(updateAnnouncementRequest, addNewAnnouncementResponse.getAnnouncementId()));
    }

    @Test
    void testCloseAnnouncementWithValidUserIdShouldNotThrowAnnouncementNotFoundException() throws AnnouncementNotFoundException {
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
    void testCloseAnnouncementWithInvalidAnnouncementIdShouldThrowAnnouncementNotFoundException() {
        //GIVEN
        validUpdateAnnouncementRequest();
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        //THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.announcementService.closeAnnouncement(INVALID_ANNOUNCEMENT_ID, addNewAnnouncementResponse.getUserId()));
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

    private UpdateAnnouncementRequest validUpdateAnnouncementRequest() {
        return UpdateAnnouncementRequest
                .builder()
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
                .build();
    }

}