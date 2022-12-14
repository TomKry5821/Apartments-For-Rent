package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.admin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.admin.AdminService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photo.PhotoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@Transactional
class AdminServiceImplTest {

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
    private AdminService adminService;

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementContentRepository announcementContentRepository;

    @Autowired
    private AnnouncementDetailsRepository announcementDetailsRepository;

    @Autowired
    private AddressDetailsRepository addressDetailsRepository;

    @Autowired
    private PhotoRepository photoRepository;

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
        Assertions.assertTrue(this.announcementContentRepository.findAll().isEmpty());
        Assertions.assertTrue(this.announcementDetailsRepository.findAll().isEmpty());
        Assertions.assertTrue(this.addressDetailsRepository.findAll().isEmpty());
        Assertions.assertTrue(this.photoRepository.findAll().isEmpty());
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