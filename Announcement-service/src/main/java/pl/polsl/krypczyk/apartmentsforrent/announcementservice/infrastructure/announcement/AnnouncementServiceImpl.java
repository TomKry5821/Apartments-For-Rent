package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
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
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.InvalidUserIdException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.exception.AnnouncementAlreadyObservedException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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
    public AddNewAnnouncementResponse addNewAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                         Long requesterId) throws InvalidUserIdException {
        log.info("Started creating announcement");

        this.checkIsUserIdValidElseThrowInvalidUser(addNewAnnouncementRequest.getUserId(), requesterId);

        var announcement = this.entityFactory.createAnnouncementEntity(addNewAnnouncementRequest);
        var response = this.responseFactory.createAddNewAnnouncementResponse(announcement, addNewAnnouncementRequest);

        log.info("Successfully created announcement - " + announcement);
        return response;
    }

    @Override
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(Long announcementId) throws AnnouncementNotFoundException {
        log.info("Started retrieving announcement details with provided id - " + announcementId);
        var announcement = this.getAnnouncementElseThrowAnnouncementNotFound(announcementId);

        var getAnnouncementWithDetailsResponse = this.responseFactory.createGetAnnouncementWithAllDetailsResponse(announcement);

        log.info("Successfully retrieved announcement details - " + getAnnouncementWithDetailsResponse);
        return getAnnouncementWithDetailsResponse;
    }

    @Override
    public UpdateAnnouncementResponse updateAnnouncement(UpdateAnnouncementRequest updateAnnouncementRequest,
                                                         Long announcementId,
                                                         Long requesterId) throws AnnouncementNotFoundException, ClosedAnnouncementException, InvalidUserIdException {
        log.info("Started updating announcement with id - " + announcementId + " By user with id - " + requesterId);

        var announcement = this.getAnnouncementElseThrowAnnouncementNotFound(announcementId);
        this.checkIsUserIdValidElseThrowInvalidUser(announcement.getUserId(), requesterId);
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
    public void closeAnnouncement(Long announcementId, Long requesterId) throws AnnouncementNotFoundException, InvalidUserIdException {
        log.info("Started closing announcement with id - " + announcementId + " by user with id - " + requesterId);

        var announcement = this.getAnnouncementElseThrowAnnouncementNotFound(announcementId);
        this.checkIsUserIdValidElseThrowInvalidUser(announcement.getUserId(), requesterId);

        announcement.setIsClosed(true);
        CompletableFuture.runAsync(() -> this.observedAnnouncementRepository.removeObservedAnnouncementEntitiesByAnnouncementEntity_Id(announcementId));

        log.info("Successfully closed announcement with id - " + announcementId + " by user with id - " + requesterId);
        this.announcementRepository.save(announcement);
    }

    @Override
    public ObserveAnnouncementResponse observeAnnouncement(Long announcementId, Long userId, Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException, AnnouncementAlreadyObservedException, ClosedAnnouncementException {
        log.info("Started observing announcement with id - " + announcementId + " by user with id - " + userId);
        this.checkIsUserIdValidElseThrowInvalidUser(userId, requesterId);

        var announcement = this.getAnnouncementElseThrowAnnouncementNotFound(announcementId);
        if (this.observedAnnouncementRepository.existsByAnnouncementEntityAndObservingUserId(announcement, userId))
            throw new AnnouncementAlreadyObservedException();
        if (announcement.getIsClosed())
            throw new ClosedAnnouncementException();


        var observedAnnouncement = this.entityFactory.createObservedAnnouncementEntity(announcement, userId);

        log.info("Successfully observed announcement with id - " + announcementId + " by user with id - " + userId);
        return this.responseFactory.createObserveAnnouncementResponse(observedAnnouncement.getObservingUserId(), announcementId);
    }

    @Override
    public void unobserveAnnouncement(Long announcementId, Long userId, Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException {
        log.info("Started unobserving announcement with id - " + announcementId + " by user with id - " + userId);

        this.checkIsUserIdValidElseThrowInvalidUser(userId, requesterId);
        var announcement = this.getAnnouncementElseThrowAnnouncementNotFound(announcementId);

        this.observedAnnouncementRepository.removeObservedAnnouncementEntityByAnnouncementEntityAndObservingUserId(announcement, userId);

        log.info("Successfully unobserved announcement with id" + announcementId + " by user with id - " + userId);
    }

    private void checkIsUserIdValidElseThrowInvalidUser(Long userId, Long requesterId) throws InvalidUserIdException {
        if (!userId.equals(requesterId))
            throw new InvalidUserIdException();
    }

    private AnnouncementEntity getAnnouncementElseThrowAnnouncementNotFound(Long announcementId) throws AnnouncementNotFoundException {
        var announcement = this.announcementRepository.findById(announcementId);
        if (announcement.isEmpty())
            throw new AnnouncementNotFoundException();
        return announcement.get();
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

    @Override
    public void deleteObservedAnnouncements(Long userId) {
        log.info("Started deleting observed announcements with user id " + userId);

        this.observedAnnouncementRepository.removeObservedAnnouncementEntitiesByObservingUserId(userId);

        log.info("Successfully deleted observed announcements with user id " + userId);
    }

}
