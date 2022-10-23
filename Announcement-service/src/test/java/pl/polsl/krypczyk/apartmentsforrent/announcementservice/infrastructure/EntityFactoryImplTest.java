package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

@SpringBootTest
class EntityFactoryImplTest {

    private static final Long USER_ID = 1L;

    private static final String TITLE = "Title";

    private static final String MAIN_PHOTO_PATH = "test/path";

    private static final Integer ROOMS_NUMBER = 2;

    private static final LocalDate RENTAL_TERM = LocalDate.MAX;

    private static final BigDecimal CAUTION = new BigDecimal("1200.50");

    private static final BigDecimal RENTAL_AMOUNT = new BigDecimal("1200.50");

    private static final String CONTENT = "Content";

    private static final List<String> PHOTO_PATHS = List.of("test/path/1", "test/path/2");

    private static final String DISTRICT = "District";

    private static final String CITY = "City";

    private static final String ZIP_CODE = "00-123";

    private static final String STREET = "Street";

    private static final String BUILDING_NUMBER = "12a";

    private static final Integer LOCAL_NUMBER = 4;

    @Autowired
    private EntityFactory entityFactory;

    @Test
    void testCreateAnnouncementEntityWithValidCreateAnnouncementRequestShouldReturnExpectedAnnouncementEntity() {
        //GIVEN
        var addAnnouncementRequest = validAddNewAnnouncementRequest();
        var expected = validAnnouncementEntity();

        //WHEN
        var actual = this.entityFactory.createAnnouncementEntity(addAnnouncementRequest, validAnnouncementDetailsEntity());

        //THEN
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
        Assertions.assertEquals(expected.getDistrict(), actual.getDistrict());
        Assertions.assertFalse(expected.getIsClosed());
    }

    @Test
    void testCreateAnnouncementDetailsEntityWithValidCreateAnnouncementRequestShouldReturnExpectedAnnouncementDetailsEntity() {
        //GIVEN
        var addAnnouncementRequest = validAddNewAnnouncementRequest();
        var expected = validAnnouncementDetailsEntity();

        //WHEN
        var actual = this.entityFactory.createAnnouncementDetailsEntity(addAnnouncementRequest, validAnnouncementContentEntity(), validAddressDetailsEntity());

        //THEN
        Assertions.assertEquals(expected.getMainPhotoPath(), actual.getMainPhotoPath());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getCaution(), actual.getCaution());
        Assertions.assertEquals(expected.getRoomsNumber(), actual.getRoomsNumber());
        Assertions.assertEquals(expected.getRentalAmount(), actual.getRentalAmount());
        Assertions.assertEquals(expected.getRentalTerm(), actual.getRentalTerm());
    }

    @Test
    void testCreateAnnouncementContentEntityWithValidCreateAnnouncementRequestShouldReturnExpectedAnnouncementContentEntity() {
        //GIVEN
        var addAnnouncementRequest = validAddNewAnnouncementRequest();
        var expected = validAnnouncementContentEntity();

        //WHEN
        var actual = this.entityFactory.createAnnouncementContentEntity(addAnnouncementRequest);

        //THEN
        Assertions.assertEquals(expected.getContent(), actual.getContent());
        Assertions.assertEquals(expected.getPhotoPaths().stream().toList().get(0).getPhotoPath(), actual.getPhotoPaths().stream().toList().get(0).getPhotoPath());
    }

    @Test
    void testCreateAddressDetailsEntityWithValidCreateAnnouncementRequestShouldReturnExpectedAddressDetailsEntity() {

        //GIVEN
        var addAnnouncementRequest = validAddNewAnnouncementRequest();
        var expected = validAddressDetailsEntity();

        //WHEN
        var actual = this.entityFactory.createAddressDetailsEntity(addAnnouncementRequest);

        //THEN
        Assertions.assertEquals(expected.getLocalNumber(), actual.getLocalNumber());
        Assertions.assertEquals(expected.getBuildingNumber(), actual.getBuildingNumber());
        Assertions.assertEquals(expected.getZipCode(), actual.getZipCode());
        Assertions.assertEquals(expected.getStreet(), actual.getStreet());

    }

    @Test
    @Transactional
    void testCreateObservedAnnouncementEntityWithValidAnnouncementAndUserIdShouldReturnExpectedObservedAnnouncementEntity() {
        //GIVEN
        var announcement = validAnnouncementEntity();
        var expected = validObservedAnnouncementEntity(announcement);

        //WHEN
        var actual = this.entityFactory.createObservedAnnouncementEntity(announcement, USER_ID);

        //THEN
        Assertions.assertEquals(expected.getObservingUserId(), actual.getObservingUserId());
        Assertions.assertEquals(expected.getAnnouncementEntity(), actual.getAnnouncementEntity());
    }

    private AnnouncementEntity validAnnouncementEntity() {
        var announcement = new AnnouncementEntity();
        announcement.setCity(CITY);
        announcement.setDistrict(DISTRICT);
        announcement.setUserId(USER_ID);
        announcement.setIsClosed(false);
        return announcement;
    }

    private AnnouncementDetailsEntity validAnnouncementDetailsEntity() {
        var announcementDetails = new AnnouncementDetailsEntity();
        announcementDetails.setRentalAmount(RENTAL_AMOUNT);
        announcementDetails.setCaution(CAUTION);
        announcementDetails.setRoomsNumber(ROOMS_NUMBER);
        announcementDetails.setTitle(TITLE);
        announcementDetails.setMainPhotoPath(MAIN_PHOTO_PATH);
        announcementDetails.setRentalTerm(RENTAL_TERM);
        announcementDetails.setAddressDetailsEntity(validAddressDetailsEntity());
        announcementDetails.setAnnouncementContent(validAnnouncementContentEntity());
        return announcementDetails;
    }

    private AddressDetailsEntity validAddressDetailsEntity() {
        var addressDetails = new AddressDetailsEntity();
        addressDetails.setLocalNumber(LOCAL_NUMBER);
        addressDetails.setBuildingNumber(BUILDING_NUMBER);
        addressDetails.setZipCode(ZIP_CODE);
        addressDetails.setStreet(STREET);
        return addressDetails;
    }

    private AnnouncementContentEntity validAnnouncementContentEntity() {
        var announcementContent = new AnnouncementContentEntity();
        var photoPath = new PhotoPathEntity();
        photoPath.setPhotoPath(PHOTO_PATHS.get(0));
        var photoPath2 = new PhotoPathEntity();
        photoPath2.setPhotoPath(PHOTO_PATHS.get(1));
        announcementContent.setContent(CONTENT);
        announcementContent.setPhotoPaths(List.of(photoPath, photoPath2));
        return announcementContent;
    }

    private ObservedAnnouncementEntity validObservedAnnouncementEntity(AnnouncementEntity announcement){
        var observedAnnouncement = new ObservedAnnouncementEntity();
        observedAnnouncement.setObservingUserId(USER_ID);
        observedAnnouncement.setAnnouncementEntity(announcement);
        return observedAnnouncement;
    }

    private AddNewAnnouncementRequest validAddNewAnnouncementRequest() {
        return AddNewAnnouncementRequest
                .builder()
                .buildingNumber(BUILDING_NUMBER)
                .caution(CAUTION)
                .city(CITY)
                .content(CONTENT)
                .district(DISTRICT)
                .localNumber(LOCAL_NUMBER)
                .mainPhotoPath(MAIN_PHOTO_PATH)
                .photoPaths(PHOTO_PATHS)
                .rentalAmount(RENTAL_AMOUNT)
                .rentalTerm(RENTAL_TERM)
                .roomsNumber(ROOMS_NUMBER)
                .street(STREET)
                .title(TITLE)
                .userId(USER_ID)
                .zipCode(ZIP_CODE)
                .build();
    }

}