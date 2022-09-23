package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.adressdetails.AddressDetailsRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementMapper;
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
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.observedannouncement.ObservedAnnouncementRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final AnnouncementMapper announcementMapper = Mappers.getMapper(AnnouncementMapper.class);

    @Override
    public Collection<AnnouncementDTO> getAllActiveAnnouncements() {
        log.info("Started retrieving all active announcements");

        var announcements = this.announcementRepository.findAnnouncementEntitiesByIsClosed(false);

        Collection<AnnouncementDTO> announcementDTOS = new ArrayList<>();
        announcements.forEach(a -> announcementDTOS.add(this.createAnnouncementDTO(a)));

        log.info("Successfully retrieved all active announcements");
        return announcementDTOS;
    }

    private AnnouncementDTO createAnnouncementDTO(AnnouncementEntity announcement) {
        var announcementDetails = announcement.getAnnouncementDetailsEntity();
        var announcementDTO = announcementMapper.announcementEntityToAnnouncementDTO(announcement);
        var announcementDetailsDTO = announcementMapper.announcementDetailsEntityToAnnouncementDetailsDTO(announcementDetails);
        announcementDTO.setAnnouncementDetailsDTO(announcementDetailsDTO);
        announcementDTO.setDistrict(announcement.getDistrict());
        announcementDTO.setCity(announcementDTO.getCity());

        return announcementDTO;
    }

    @Override
    public AddNewAnnouncementResponse addNewAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                         Long requesterId) throws InvalidUserIdException {
        log.info("Started creating announcement");

        this.checkIsUserIdValidElseThrowInvalidUser(addNewAnnouncementRequest.getUserId(), requesterId);

        var announcement = this.createAndSaveAnnouncement(addNewAnnouncementRequest);
        var response = this.announcementMapper.createAnnouncementRequestToCreateAnnouncementResponse(addNewAnnouncementRequest);
        response.setAnnouncementId(announcement.getId());
        response.setCreationDate(announcement.getCreationDate());
        response.setIsClosed(announcement.getIsClosed());

        log.info("Successfully created announcement - " + announcement);
        return response;
    }

    private AnnouncementEntity createAndSaveAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcement = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementEntity(addNewAnnouncementRequest);
        announcement.setAnnouncementDetailsEntity(this.createAnnouncementDetailsEntity(addNewAnnouncementRequest));
        announcement.setCreationDate(LocalDate.now());
        announcement.setIsClosed(false);

        return this.announcementRepository.save(announcement);
    }

    private AnnouncementDetailsEntity createAnnouncementDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcementDetails = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementDetailsEntity(addNewAnnouncementRequest);
        announcementDetails.setAnnouncementContent(this.createAnnouncementContentEntity(addNewAnnouncementRequest));
        announcementDetails.setAddressDetailsEntity(this.createAddressDetailsEntity(addNewAnnouncementRequest));

        return this.announcementDetailsRepository.save(announcementDetails);
    }

    private AnnouncementContentEntity createAnnouncementContentEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcementContent = this.announcementMapper.addAnnouncementRequestDtoToAnnouncementContentEntity(addNewAnnouncementRequest);

        var photoPaths = new ArrayList<PhotoPathEntity>();
        addNewAnnouncementRequest.getPhotoPaths().forEach((r) -> {
            var photoPath = this.announcementMapper.photoPathToPhotoPathEntity(r);
            photoPaths.add(this.photoPathRepository.save(photoPath));
        });
        announcementContent.setPhotoPaths(photoPaths);

        return announcementContentRepository.save(announcementContent);
    }

    private AddressDetailsEntity createAddressDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var addressDetails = this.announcementMapper.addAnnouncementRequestDtoToAddressDetailsEntity(addNewAnnouncementRequest);
        return this.addressDetailsRepository.save(addressDetails);
    }

    @Override
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(Long announcementId) throws AnnouncementNotFoundException {
        log.info("Started retrieving announcement details with provided id - " + announcementId);
        var announcement = this.getAnnouncementOrThrowAnnouncementNotFound(announcementId);

        var getAnnouncementWithDetailsResponse = this.buildResponse(announcement);

        log.info("Successfully retrieved announcement details - " + getAnnouncementWithDetailsResponse);
        return getAnnouncementWithDetailsResponse;
    }

    private GetAnnouncementWithAllDetailsResponse buildResponse(AnnouncementEntity announcement) {
        var announcementDetails = announcement.getAnnouncementDetailsEntity();
        var addressDetails = announcementDetails.getAddressDetailsEntity();
        var announcementContent = announcementDetails.getAnnouncementContent();
        var photoPaths = announcementContent.getPhotoPaths();

        return GetAnnouncementWithAllDetailsResponse
                .builder()
                .creationDate(announcement.getCreationDate())
                .userId(announcement.getUserId())
                .isClosed(announcement.getIsClosed())
                .district(announcement.getDistrict())
                .city(announcement.getCity())
                .title(announcementDetails.getTitle())
                .mainPhotoPath(announcementDetails.getMainPhotoPath())
                .roomsNumber(announcementDetails.getRoomsNumber())
                .rentalTerm(announcementDetails.getRentalTerm())
                .caution(announcementDetails.getCaution())
                .rentalAmount(announcementDetails.getRentalAmount())
                .street(addressDetails.getStreet())
                .zipCode(addressDetails.getZipCode())
                .buildingNumber(addressDetails.getBuildingNumber())
                .localNumber(addressDetails.getLocalNumber())
                .content(announcementContent.getContent())
                .photoPaths(photoPaths
                        .stream()
                        .map(PhotoPathEntity::getPhotoPath)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public UpdateAnnouncementResponse updateAnnouncement(UpdateAnnouncementRequest updateAnnouncementRequest,
                                                         Long announcementId,
                                                         Long requesterId) throws AnnouncementNotFoundException, ClosedAnnouncementException, InvalidUserIdException {
        log.info("Started updating announcement with id - " + announcementId + " By user with id - " + requesterId);

        var announcement = this.getAnnouncementOrThrowAnnouncementNotFound(announcementId);
        this.checkIsUserIdValidElseThrowInvalidUser(announcement.getUserId(), requesterId);
        if (announcement.getIsClosed().equals(true))
            throw new ClosedAnnouncementException();

        this.updateAnnouncementEntity(announcement, updateAnnouncementRequest);

        var updateAnnouncementResponse = this.announcementMapper.updateAnnouncementRequestToUpdateAnnouncementResponse(updateAnnouncementRequest);

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

        var announcement = this.getAnnouncementOrThrowAnnouncementNotFound(announcementId);
        this.checkIsUserIdValidElseThrowInvalidUser(announcement.getUserId(), requesterId);

        announcement.setIsClosed(true);

        log.info("Successfully closed announcement with id - " + announcementId + " by user with id - " + requesterId);
        this.announcementRepository.save(announcement);
    }

    @Override
    public ObserveAnnouncementResponse observeAnnouncement(Long announcementId, Long userId, Long requesterId) throws InvalidUserIdException, AnnouncementNotFoundException {
        log.info("Started observing announcement with id - " + announcementId + " by user with id - " + userId);
        this.checkIsUserIdValidElseThrowInvalidUser(userId, requesterId);

        var announcement = this.getAnnouncementOrThrowAnnouncementNotFound(announcementId);

        var observedAnnouncement = new ObservedAnnouncementEntity();
        observedAnnouncement.setObservingUserId(userId);
        observedAnnouncement.setAnnouncementEntity(announcement);
        this.observedAnnouncementRepository.save(observedAnnouncement);

        log.info("Successfully observed announcement with id - " + announcementId + " by user with id - " + userId);
        return this.buildObserveAnnouncementResponse(userId, announcementId);
    }

    private void checkIsUserIdValidElseThrowInvalidUser(Long userId, Long requesterId) throws InvalidUserIdException {
        if (!userId.equals(requesterId))
            throw new InvalidUserIdException();
    }

    private AnnouncementEntity getAnnouncementOrThrowAnnouncementNotFound(Long announcementId) throws AnnouncementNotFoundException {
        var announcement = this.announcementRepository.findById(announcementId);
        if (announcement.isEmpty())
            throw new AnnouncementNotFoundException();
        return announcement.get();
    }

    private ObserveAnnouncementResponse buildObserveAnnouncementResponse(Long userId, Long announcementId) {
        return ObserveAnnouncementResponse
                .builder()
                .userId(userId)
                .announcementId(announcementId)
                .build();
    }

}
