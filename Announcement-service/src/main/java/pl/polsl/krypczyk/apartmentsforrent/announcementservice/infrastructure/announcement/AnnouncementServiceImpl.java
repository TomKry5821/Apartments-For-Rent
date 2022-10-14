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
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.utils.AnnouncementUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementContentRepository announcementContentRepository;
    private final AddressDetailsRepository addressDetailsRepository;
    private final AnnouncementDetailsRepository announcementDetailsRepository;
    private final PhotoPathRepository photoPathRepository;
    private final ObservedAnnouncementRepository observedAnnouncementRepository;
    private final ResponseFactory responseFactory;
    private final EntityFactory entityFactory;
    private final AnnouncementUtils announcementUtils;

    @Override
    public Collection<AnnouncementDTO> getAllActiveAnnouncements() {
        log.info("Started retrieving all active announcements");

        var announcements = this.announcementRepository.findAnnouncementEntitiesByIsClosed(false);

        Collection<AnnouncementDTO> announcementDTOS = new ArrayList<>();
        announcements.forEach(a -> announcementDTOS.add(this.responseFactory.createAnnouncementDTO(a)));

        log.info("Successfully retrieved all active announcements");
        return announcementDTOS;
    }

    @Override
    public AddNewAnnouncementResponse addNewAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        log.info("Started creating announcement");

        var announcement = this.entityFactory.createAnnouncementEntity(addNewAnnouncementRequest);
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
                                                         Long announcementId) throws AnnouncementNotFoundException, ClosedAnnouncementException {
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
                                          UpdateAnnouncementRequest updateAnnouncementRequest) {

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
                                                 UpdateAnnouncementRequest updateAnnouncementRequest) {
        var title = updateAnnouncementRequest.getTitle();
        if (!Objects.isNull(title))
            announcementDetails.setTitle(title);
        var mainPhotoPath = updateAnnouncementRequest.getMainPhotoPath();
        if (!Objects.isNull(mainPhotoPath))
            announcementDetails.setMainPhotoPath(mainPhotoPath);
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
        var photoPaths = updateAnnouncementRequest.getPhotoPaths();
        if (!Objects.isNull(photoPaths)) {
            Collection<PhotoPathEntity> photoPathEntities = new ArrayList<>();
            photoPaths.forEach(pp -> {
                var photoPath = new PhotoPathEntity();
                photoPath.setPhotoPath(pp);
                this.photoPathRepository.save(photoPath);
                photoPathEntities.add(photoPath);
            });
            announcementContent.getPhotoPaths().clear();
            announcementContent.setPhotoPaths(photoPathEntities);
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
