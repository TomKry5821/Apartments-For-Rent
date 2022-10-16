package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.admin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
class AdminServiceImplTest {

    private static final String CITY = "City";
    private static final BigDecimal CAUTION = BigDecimal.valueOf(10.55);
    private static final String CONTENT = "Content";
    private static final String DISTRICT = "District";
    private static final int LOCAL_NUMBER = 1;
    private static final String BUILDING_NUMBER = "1A";
    private static final String STREET = "Street";
    private static final String TITLE = "Title";
    private static final List<String> PHOTO_PATHS = List.of("test", "test2");
    private static final String MAIN_PHOTO_PATH = "path";
    private static final BigDecimal RENTAL_AMOUNT = BigDecimal.valueOf(10.55);
    private static final int ROOMS_NUMBER = 1;
    private static final String ZIP_CODE = "11-111";
    private static final LocalDate RENTAL_TERM = LocalDate.now();
    private static final long USER_ID = 1L;
    private static final long INVALID_ANNOUNCEMENT_ID = 0L;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @BeforeEach
    void deleteDbContent() {
        this.announcementRepository.deleteAll();
    }

    @Test
    void testDeleteAnnouncementWithValidAnnouncementIdShouldReturnEmptyResponseAndShouldNotThrowAnnouncementNotfoundException() throws AnnouncementNotFoundException {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        var addNewAnnouncementResponse = this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        this.adminService.deleteAnnouncement(addNewAnnouncementResponse.getAnnouncementId());

        //THEN
        Assertions.assertDoesNotThrow(AnnouncementNotFoundException::new);
        Assertions.assertTrue(this.announcementRepository.findAll().isEmpty());
    }

    @Test
    void testDeleteAnnouncementWithInvalidAnnouncementIdShouldReturnNotEmptyResponseAndShouldThrowAnnouncementNotFoundException() {
        //GIVEN
        var addNewAnnouncementRequest = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(addNewAnnouncementRequest);

        //WHEN
        //THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.adminService.deleteAnnouncement(INVALID_ANNOUNCEMENT_ID));
        Assertions.assertFalse(this.announcementRepository.findAll().isEmpty());
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
                .photoPaths(PHOTO_PATHS)
                .mainPhotoPath(MAIN_PHOTO_PATH)
                .rentalAmount(RENTAL_AMOUNT)
                .roomsNumber(ROOMS_NUMBER)
                .zipCode(ZIP_CODE)
                .rentalTerm(RENTAL_TERM)
                .userId(USER_ID)
                .build();
    }
}