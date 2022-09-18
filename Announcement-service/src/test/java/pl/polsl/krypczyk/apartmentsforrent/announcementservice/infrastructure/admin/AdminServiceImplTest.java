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
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.AddNewAnnouncementRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class AdminServiceImplTest {

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
    void testDeleteAnnouncement_WithValidAnnouncementId() throws InvalidUserIdException, AnnouncementNotFoundException {
        //GIVEN
        var announcementRequest = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(announcementRequest, announcementRequest.getUserId());

        //WHEN
        this.adminService.deleteAnnouncement(9L);

        //THEN
        Assertions.assertDoesNotThrow(AnnouncementNotFoundException::new);
        Assertions.assertTrue(this.announcementRepository.findAll().isEmpty());
    }

    @Test
    void testDeleteAnnouncement_WithInvalidAnnouncementId() throws InvalidUserIdException {
        //GIVEN
        var announcementRequest = validAnnouncementRequest();
        this.announcementService.addNewAnnouncement(announcementRequest, announcementRequest.getUserId());

        //WHEN AND THEN
        Assertions.assertThrows(AnnouncementNotFoundException.class, () ->
                this.adminService.deleteAnnouncement(100L));
        Assertions.assertFalse(this.announcementRepository.findAll().isEmpty());
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
}