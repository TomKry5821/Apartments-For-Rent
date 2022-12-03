package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.EntityFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementService;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.ClosedAnnouncementException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photo.PhotoEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photo.PhotoRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.utils.AnnouncementUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {

    private static final boolean ACTIVE = false;
    private final AnnouncementRepository announcementRepository;
    private final AnnouncementContentRepository announcementContentRepository;
    private final AddressDetailsRepository addressDetailsRepository;
    private final AnnouncementDetailsRepository announcementDetailsRepository;
    private final PhotoRepository photoRepository;
    private final ObservedAnnouncementRepository observedAnnouncementRepository;
    private final ResponseFactory responseFactory;
    private final EntityFactory entityFactory;
    private final AnnouncementUtils announcementUtils;

    @Override
    public Collection<AnnouncementDTO> getAllActiveAnnouncements(Long userId) {
        log.info("Started retrieving all active announcements");

        var announcements = (Objects.isNull(userId)) ?
                this.announcementRepository.findAnnouncementEntitiesByIsClosed(false):
                this.announcementRepository.findAnnouncementEntitiesByIsClosedAndUserId(ACTIVE, userId);

        Collection<AnnouncementDTO> announcementDTOS = new ArrayList<>();
        announcements.forEach(a -> announcementDTOS.add(this.responseFactory.createAnnouncementDTO(a)));

        log.info("Successfully retrieved all active announcements");
        return announcementDTOS;
    }

    @Override
    public AddNewAnnouncementResponse addNewAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        log.info("Started creating announcement");

        var announcementContent = this.entityFactory.createAnnouncementContentEntity(addNewAnnouncementRequest);
        this.announcementContentRepository.save(announcementContent);
        var addressDetails = this.entityFactory.createAddressDetailsEntity(addNewAnnouncementRequest);
        this.addressDetailsRepository.save(addressDetails);
        var announcementDetails = this.entityFactory.createAnnouncementDetailsEntity(addNewAnnouncementRequest, announcementContent, addressDetails);
        this.announcementDetailsRepository.save(announcementDetails);
        var announcement = this.entityFactory.createAnnouncementEntity(addNewAnnouncementRequest, announcementDetails);
        this.announcementRepository.save(announcement);

        var response = this.responseFactory.createAddNewAnnouncementResponse(announcement, addNewAnnouncementRequest);

        log.info("Successfully created announcement - " + announcement);
        return response;
    }

    @Override
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(Long announcementId) throws AnnouncementNotFoundException {
        log.info("Started retrieving announcement details with provided id - " + announcementId);
        var announcement = this.announcementUtils.getAnnouncementElseThrowAnnouncementNotFound(announcementId);

        var getAnnouncementWithDetailsResponse = this.responseFactory.createGetAnnouncementWithAllDetailsResponse(announcement);

        log.info("Successfully retrieved announcement details - " + getAnnouncementWithDetailsResponse);
        return getAnnouncementWithDetailsResponse;
    }

    @Override
    public UpdateAnnouncementResponse updateAnnouncement(UpdateAnnouncementRequest updateAnnouncementRequest,
                                                         Long announcementId) throws AnnouncementNotFoundException, ClosedAnnouncementException, IOException {
        log.info("Started updating announcement with id - " + announcementId + " By user with id - " + updateAnnouncementRequest.getUserId());

        var announcement = this.announcementUtils.getAnnouncementElseThrowAnnouncementNotFound(announcementId);
        if (announcement.getIsClosed().equals(true))
            throw new ClosedAnnouncementException();

        this.updateAnnouncementEntity(announcement, updateAnnouncementRequest);

        var updateAnnouncementResponse = this.responseFactory.createUpdateAnnouncementResponse(updateAnnouncementRequest);

        log.info("Successfully updated announcement with id " + announcementId + ": " + updateAnnouncementResponse);
        return updateAnnouncementResponse;
    }

    private void updateAnnouncementEntity(AnnouncementEntity announcement,
                                          UpdateAnnouncementRequest updateAnnouncementRequest) throws IOException {

        var district = updateAnnouncementRequest.getDistrict();
        if (!Objects.isNull(district))
            announcement.setDistrict(district);
        var city = updateAnnouncementRequest.getCity();
        if (!Objects.isNull(city))
            announcement.setCity(city);
        var announcementDetails = announcement.getAnnouncementDetailsEntity();
        this.updateAnnouncementDetailsEntity(announcementDetails, updateAnnouncementRequest);
        announcement.setAnnouncementDetailsEntity(announcementDetails);

        this.announcementRepository.save(announcement);
    }

    private void updateAnnouncementDetailsEntity(AnnouncementDetailsEntity announcementDetails,
                                                 UpdateAnnouncementRequest updateAnnouncementRequest) throws IOException {
        var title = updateAnnouncementRequest.getTitle();
        if (!Objects.isNull(title))
            announcementDetails.setTitle(title);
        var mainPhotoPath = updateAnnouncementRequest.getMainPhoto();
        if (!Objects.isNull(mainPhotoPath))
            announcementDetails.setMainPhoto(mainPhotoPath.getBytes());
        var roomsNumber = updateAnnouncementRequest.getRoomsNumber();
        if (!Objects.isNull(roomsNumber))
            announcementDetails.setRoomsNumber(roomsNumber);
        var rentalTerm = updateAnnouncementRequest.getRentalTerm();
        if (!Objects.isNull(rentalTerm))
            announcementDetails.setRentalTerm(rentalTerm);
        var caution = updateAnnouncementRequest.getCaution();
        if (!Objects.isNull(caution))
            announcementDetails.setCaution(caution);
        var rentalAmount = updateAnnouncementRequest.getRentalAmount();
        if (!Objects.isNull(rentalAmount))
            announcementDetails.setRentalAmount(rentalAmount);

        var announcementContent = announcementDetails.getAnnouncementContent();
        this.updateAnnouncementContentEntity(announcementContent, updateAnnouncementRequest);

        var addressDetails = announcementDetails.getAddressDetailsEntity();
        this.updateAddressDetailsEntity(addressDetails, updateAnnouncementRequest);

        this.announcementDetailsRepository.save(announcementDetails);
    }

    private void updateAnnouncementContentEntity(AnnouncementContentEntity announcementContent,
                                                 UpdateAnnouncementRequest updateAnnouncementRequest) {
        var content = updateAnnouncementRequest.getContent();
        if (!Objects.isNull(content))
            announcementContent.setContent(content);
        var photos = updateAnnouncementRequest.getPhotos();
        if (!Objects.isNull(photos)) {
            Collection<PhotoEntity> photoPathEntities = new ArrayList<>();
            photos.forEach(pp -> {
                var photoPath = new PhotoEntity();
                try {
                    photoPath.setPhoto(pp.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                this.photoRepository.save(photoPath);
                photoPathEntities.add(photoPath);
            });
            announcementContent.getPhotos().clear();
            announcementContent.setPhotos(photoPathEntities);
        }
        this.announcementContentRepository.save(announcementContent);
    }

    private void updateAddressDetailsEntity(AddressDetailsEntity addressDetails,
                                            UpdateAnnouncementRequest updateAnnouncementRequest) {
        var street = updateAnnouncementRequest.getStreet();
        if (!Objects.isNull(street))
            addressDetails.setStreet(street);
        var zipCode = updateAnnouncementRequest.getZipCode();
        if (!Objects.isNull(zipCode))
            addressDetails.setZipCode(zipCode);
        var buildingNumber = updateAnnouncementRequest.getBuildingNumber();
        if (!Objects.isNull(buildingNumber))
            addressDetails.setBuildingNumber(buildingNumber);
        var localNumber = updateAnnouncementRequest.getLocalNumber();
        if (!Objects.isNull(localNumber))
            addressDetails.setLocalNumber(localNumber);

        this.addressDetailsRepository.save(addressDetails);
    }

    @Override
    public void closeAnnouncement(Long announcementId, Long userId) throws AnnouncementNotFoundException {
        log.info("Started closing announcement with id - " + announcementId + " by user with id - " + userId);

        var announcement = this.announcementUtils.getAnnouncementElseThrowAnnouncementNotFound(announcementId);
        announcement.setIsClosed(true);

        this.observedAnnouncementRepository.removeObservedAnnouncementEntitiesByAnnouncementEntity_Id(announcementId);

        log.info("Successfully closed announcement with id - " + announcementId + " by user with id - " + userId);
        this.announcementRepository.save(announcement);
    }

    @Override
    public void closeUserAnnouncements(Long userId) {
        log.info("Started inactivating all announcements with user id " + userId);

        var announcementsToClose = this.announcementRepository.findAnnouncementEntitiesByUserId(userId);

        for (var announcementToClose : announcementsToClose) {
            log.info("Inactivating announcement with id " + announcementToClose.getId());
            announcementToClose.setIsClosed(true);
            this.announcementRepository.save(announcementToClose);
        }

        log.info("Successfully inactivated all announcements with user id " + userId);
    }

    @Override
    public void deleteUserAnnouncements(Long userId) {
        log.info("Started deleting all announcements with user id " + userId);

        this.announcementRepository.deleteAnnouncementEntitiesByUserId(userId);

        log.info("Successfully deleted all announcements with user id " + userId);
    }

}
