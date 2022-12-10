package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto.ObservedAnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photo.PhotoEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class ResponseFactoryImplTest {

    private static final Long USER_ID = 1L;

    private static final String TITLE = "Title";

    private static final byte[] MAIN_PHOTO = new byte[5];
    private static final Integer ROOMS_NUMBER = 2;

    private static final LocalDate RENTAL_TERM = LocalDate.MAX;

    private static final BigDecimal CAUTION = new BigDecimal("1200.50");

    private static final BigDecimal RENTAL_AMOUNT = new BigDecimal("1200.50");

    private static final String CONTENT = "Content";

    private static final List<byte[]> PHOTOS = List.of(
            new byte[3],
            new byte[4]);
    private static final String DISTRICT = "District";

    private static final String CITY = "City";

    private static final String ZIP_CODE = "00-123";

    private static final String STREET = "Street";

    private static final String BUILDING_NUMBER = "12a";

    private static final Integer LOCAL_NUMBER = 4;
    private static final Long ANNOUNCEMENT_ID = 1L;
    private static final String USERNAME = "Nieznany użytkownik";
    private static final List<MultipartFile> PHOTO_FILES = List.of(
            new MockMultipartFile("test1", new byte[5]),
            new MockMultipartFile("test2", new byte[5]));
    private static final MultipartFile MAIN_PHOTO_FILE = new MockMultipartFile("test", new byte[5]);

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
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getDistrict(), actual.getDistrict());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
        Assertions.assertEquals(expected.getIsClosed(), actual.getIsClosed());
        Assertions.assertEquals(expected.getUsername(), actual.getUsername());
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
        Assertions.assertEquals(expected.getZipCode(), actual.getZipCode());
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
        Assertions.assertEquals(expected.getPhotos(), actual.getPhotos());
        Assertions.assertEquals(expected.getZipCode(), actual.getZipCode());
        Assertions.assertEquals(expected.getMainPhoto(), actual.getMainPhoto());
        Assertions.assertEquals(expected.getRentalTerm(), actual.getRentalTerm());
        Assertions.assertEquals(expected.getRentalAmount(), actual.getRentalAmount());
        Assertions.assertEquals(expected.getUsername(), actual.getUsername());
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
        Assertions.assertEquals(expected.getZipCode(), actual.getZipCode());
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
    void testCreateObservedAnnouncementDTOWithValidAnnouncementEntityShouldReturnExpectedObservedAnnouncementDTO() {
        //GIVEN
        var announcement = validAnnouncementWithAllDetailsEntity();
        var expected = validObservedAnnouncementDTO();

        //WHEN
        var actual = this.responseFactory.createObservedAnnouncementDTO(announcement);

        //THEN
        Assertions.assertEquals(expected.getUserId(), actual.getUserId());
        Assertions.assertEquals(expected.getAnnouncementId(), actual.getAnnouncementId());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getMainPhoto(), actual.getMainPhoto());
        Assertions.assertEquals(expected.getUsername(), actual.getUsername());
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
                .username(USERNAME)
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
        announcementDetails.setMainPhoto(MAIN_PHOTO);
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
        var photo = new PhotoEntity();
        photo.setPhoto(PHOTOS.get(0));
        var photo1 = new PhotoEntity();
        photo1.setPhoto(PHOTOS.get(1));
        announcementContent.setContent(CONTENT);
        announcementContent.setPhotos(List.of(photo, photo1));
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
                .mainPhoto(MAIN_PHOTO_FILE)
                .photos(PHOTO_FILES)
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
                .username(USERNAME)
                .title(TITLE)
                .district(DISTRICT)
                .isClosed(false)
                .localNumber(LOCAL_NUMBER)
                .mainPhoto(MAIN_PHOTO)
                .photos(PHOTOS)
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
                .mainPhoto(MAIN_PHOTO_FILE)
                .photos(PHOTO_FILES)
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
                .announcementId(ANNOUNCEMENT_ID)
                .title(TITLE)
                .mainPhoto(MAIN_PHOTO)
                .userId(USER_ID)
                .username(USERNAME)
                .build();
    }
}