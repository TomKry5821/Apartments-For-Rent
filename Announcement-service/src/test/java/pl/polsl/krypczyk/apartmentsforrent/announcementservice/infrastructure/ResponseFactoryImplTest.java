package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto.ObservedAnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class ResponseFactoryImplTest {

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
    private static final Long ANNOUNCEMENT_ID = 1L;

    @Autowired
    ResponseFactory responseFactory;

    @Test
    void testCreateAnnouncementDTOWithValidAnnouncementEntityShouldReturnExpectedAnnouncementDTO() {
        //GIVEN
        var announcement = validAnnouncementEntity();
        var expected = validAnnouncementDTO();

        //WHEN
        var actual = this.responseFactory.createAnnouncementDTO(announcement);

        //THEN
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getDistrict(), actual.getDistrict());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
        Assertions.assertEquals(expected.getIsClosed(), actual.getIsClosed());
    }

    @Test
    void testCreateAddNewAnnouncementResponseWithValidAnnouncementAndAddAnnouncementRequestShouldReturnExpectedAddAnnouncementResponse() {
        //GIVEN
        var announcement = validAnnouncementEntity();
        var addAnnouncementRequest = validAddNewAnnouncementRequest();
        var expected = validAddNewAnnouncementResponse();

        //WHEN
        var actual = this.responseFactory.createAddNewAnnouncementResponse(announcement, addAnnouncementRequest);

        //THEN
        Assertions.assertEquals(expected.getContent(), actual.getContent());
        Assertions.assertEquals(expected.getCaution(), actual.getCaution());
        Assertions.assertEquals(expected.getLocalNumber(), actual.getLocalNumber());
        Assertions.assertEquals(expected.getBuildingNumber(), actual.getBuildingNumber());
        Assertions.assertEquals(expected.getStreet(), actual.getStreet());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getRoomsNumber(), actual.getRoomsNumber());
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
        Assertions.assertEquals(expected.getDistrict(), actual.getDistrict());
        Assertions.assertEquals(expected.getPhotoPaths(), actual.getPhotoPaths());
        Assertions.assertEquals(expected.getZipCode(), actual.getZipCode());
        Assertions.assertEquals(expected.getMainPhotoPath(), actual.getMainPhotoPath());
        Assertions.assertEquals(expected.getRentalTerm(), actual.getRentalTerm());
        Assertions.assertEquals(expected.getRentalAmount(), actual.getRentalAmount());
    }

    @Test
    void testCreateGetAnnouncementWithAllDetailsResponseWithValidAnnouncementShouldReturnExpectedGetAnnouncementWithAllDetailsResponse() {
        //GIVEN
        var announcement = validAnnouncementWithAllDetailsEntity();
        var expected = validGetAnnouncementWithAllDetailsResponse();

        //WHEN
        var actual = this.responseFactory.createGetAnnouncementWithAllDetailsResponse(announcement);

        //THEN
        Assertions.assertEquals(expected.getContent(), actual.getContent());
        Assertions.assertEquals(expected.getCaution(), actual.getCaution());
        Assertions.assertEquals(expected.getLocalNumber(), actual.getLocalNumber());
        Assertions.assertEquals(expected.getBuildingNumber(), actual.getBuildingNumber());
        Assertions.assertEquals(expected.getStreet(), actual.getStreet());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getRoomsNumber(), actual.getRoomsNumber());
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
        Assertions.assertEquals(expected.getDistrict(), actual.getDistrict());
        Assertions.assertEquals(expected.getPhotoPaths(), actual.getPhotoPaths());
        Assertions.assertEquals(expected.getZipCode(), actual.getZipCode());
        Assertions.assertEquals(expected.getMainPhotoPath(), actual.getMainPhotoPath());
        Assertions.assertEquals(expected.getRentalTerm(), actual.getRentalTerm());
        Assertions.assertEquals(expected.getRentalAmount(), actual.getRentalAmount());
    }

    @Test
    void testCreateUpdateAnnouncementResponseWithValidUpdateAnnouncementRequestShouldReturnExpectedUpdateAnnouncementResponse() {
        //GIVEN
        var updateAnnouncementRequest = validUpdateAnnouncementRequest();
        var expected = validUpdateAnnouncementResponse();

        //WHEN
        var actual = this.responseFactory.createUpdateAnnouncementResponse(updateAnnouncementRequest);

        //THEN
        Assertions.assertEquals(expected.getContent(), actual.getContent());
        Assertions.assertEquals(expected.getCaution(), actual.getCaution());
        Assertions.assertEquals(expected.getLocalNumber(), actual.getLocalNumber());
        Assertions.assertEquals(expected.getBuildingNumber(), actual.getBuildingNumber());
        Assertions.assertEquals(expected.getStreet(), actual.getStreet());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getRoomsNumber(), actual.getRoomsNumber());
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
        Assertions.assertEquals(expected.getDistrict(), actual.getDistrict());
        Assertions.assertEquals(expected.getPhotoPaths(), actual.getPhotoPaths());
        Assertions.assertEquals(expected.getZipCode(), actual.getZipCode());
        Assertions.assertEquals(expected.getMainPhotoPath(), actual.getMainPhotoPath());
        Assertions.assertEquals(expected.getRentalTerm(), actual.getRentalTerm());
        Assertions.assertEquals(expected.getRentalAmount(), actual.getRentalAmount());
    }

    @Test
    void createObserveAnnouncementResponse() {
        //GIVEN
        var expected = validObserveAnnouncementResponse();

        //WHEN
        var actual = this.responseFactory.createObserveAnnouncementResponse(USER_ID, ANNOUNCEMENT_ID);

        //THEN
        Assertions.assertEquals(expected.getAnnouncementId(), actual.getAnnouncementId());
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
    }

    @Test
    void testCreateObservedAnnouncementDTOWithValidAnnouncementEntityShouldReturnExpectedObservedAnnouncmentDTO() {
        //GIVEN
        var announcement = validAnnouncementWithAllDetailsEntity();
        var expected = validObservedAnnouncementDTO();

        //WHEN
        var actual = this.responseFactory.createObservedAnnouncementDTO(announcement);

        //THEN
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getMainPhotoPath(), actual.getMainPhotoPath());
    }

    private AnnouncementEntity validAnnouncementEntity() {
        var announcement = new AnnouncementEntity();
        announcement.setCity(CITY);
        announcement.setDistrict(DISTRICT);
        announcement.setUserId(USER_ID);
        announcement.setIsClosed(false);
        return announcement;
    }

    private AnnouncementDTO validAnnouncementDTO() {
        return AnnouncementDTO
                .builder()
                .userId(USER_ID)
                .isClosed(false)
                .city(CITY)
                .district(DISTRICT)
                .build();
    }

    private AnnouncementEntity validAnnouncementWithAllDetailsEntity() {
        var announcement = new AnnouncementEntity();
        announcement.setCity(CITY);
        announcement.setDistrict(DISTRICT);
        announcement.setUserId(USER_ID);
        announcement.setIsClosed(false);
        announcement.setAnnouncementDetailsEntity(validAnnouncementDetailsEntity());
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

    private AddNewAnnouncementResponse validAddNewAnnouncementResponse() {
        return AddNewAnnouncementResponse
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
                .zipCode(ZIP_CODE)
                .userId(USER_ID)
                .build();
    }

    private GetAnnouncementWithAllDetailsResponse validGetAnnouncementWithAllDetailsResponse() {
        return GetAnnouncementWithAllDetailsResponse
                .builder()
                .buildingNumber(BUILDING_NUMBER)
                .caution(CAUTION)
                .city(CITY)
                .content(CONTENT)
                .zipCode(ZIP_CODE)
                .userId(USER_ID)
                .title(TITLE)
                .district(DISTRICT)
                .isClosed(false)
                .localNumber(LOCAL_NUMBER)
                .mainPhotoPath(MAIN_PHOTO_PATH)
                .photoPaths(PHOTO_PATHS)
                .street(STREET)
                .rentalAmount(RENTAL_AMOUNT)
                .rentalTerm(RENTAL_TERM)
                .roomsNumber(ROOMS_NUMBER)
                .build();
    }

    private UpdateAnnouncementRequest validUpdateAnnouncementRequest() {
        return UpdateAnnouncementRequest
                .builder()
                .buildingNumber(BUILDING_NUMBER)
                .caution(CAUTION)
                .city(CITY)
                .content(CONTENT)
                .zipCode(ZIP_CODE)
                .userId(USER_ID)
                .title(TITLE)
                .district(DISTRICT)
                .localNumber(LOCAL_NUMBER)
                .mainPhotoPath(MAIN_PHOTO_PATH)
                .photoPaths(PHOTO_PATHS)
                .street(STREET)
                .rentalAmount(RENTAL_AMOUNT)
                .rentalTerm(RENTAL_TERM)
                .roomsNumber(ROOMS_NUMBER)
                .build();
    }

    private UpdateAnnouncementResponse validUpdateAnnouncementResponse() {
        return UpdateAnnouncementResponse
                .builder()
                .buildingNumber(BUILDING_NUMBER)
                .caution(CAUTION)
                .city(CITY)
                .content(CONTENT)
                .zipCode(ZIP_CODE)
                .userId(USER_ID)
                .title(TITLE)
                .district(DISTRICT)
                .localNumber(LOCAL_NUMBER)
                .mainPhotoPath(MAIN_PHOTO_PATH)
                .photoPaths(PHOTO_PATHS)
                .street(STREET)
                .rentalAmount(RENTAL_AMOUNT)
                .rentalTerm(RENTAL_TERM)
                .roomsNumber(ROOMS_NUMBER)
                .build();
    }

    private ObserveAnnouncementResponse validObserveAnnouncementResponse() {
        return ObserveAnnouncementResponse
                .builder()
                .announcementId(ANNOUNCEMENT_ID)
                .userId(USER_ID)
                .build();
    }

    private ObservedAnnouncementDTO validObservedAnnouncementDTO() {
        return ObservedAnnouncementDTO
                .builder()
                .title(TITLE)
                .mainPhotoPath(MAIN_PHOTO_PATH)
                .userId(USER_ID)
                .build();
    }
}