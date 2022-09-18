package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure.announcement;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementContentRepository announcementContentRepository;
    private final AddressDetailsRepository addressDetailsRepository;
    private final AnnouncementDetailsRepository announcementDetailsRepository;
    private final PhotoPathRepository photoPathRepository;
    private final AnnouncementMapper announcementMapper = Mappers.getMapper(AnnouncementMapper.class);

    @Override
    public Collection<AnnouncementDTO> getAllActiveAnnouncements() {
        var announcements = this.announcementRepository.findAnnouncementEntitiesByIsClosed(false);

        Collection<AnnouncementDTO> announcementDTOS = new ArrayList<>();
        announcements.forEach(a -> announcementDTOS.add(this.createAnnouncementDTO(a)));
        return announcementDTOS;
    }

    private AnnouncementDTO createAnnouncementDTO(AnnouncementEntity announcement) {
        var announcementDetails = announcement.getAnnouncementDetailsEntity();
        var announcementDTO = announcementMapper.announcementEntityToAnnouncementDTO(announcement);
        var announcementDetailsDTO = announcementMapper.announcementDetailsEntityToAnnouncementDetailsDTO(announcementDetails);
        announcementDTO.setAnnouncementDetailsDTO(announcementDetailsDTO);

        return announcementDTO;
    }

    @Override
    public AddNewAnnouncementResponse addNewAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest,
                                                         Long requesterId) throws InvalidUserIdException {
        if (!this.isUserIdValid(addNewAnnouncementRequest.getUserId(), requesterId))
            throw new InvalidUserIdException();

        var announcement = this.createAndSaveAnnouncement(addNewAnnouncementRequest);
        var response = this.announcementMapper.createAnnouncementRequestToCreateAnnouncementResponse(addNewAnnouncementRequest);
        response.setAnnouncementId(announcement.getId());
        response.setCreationDate(announcement.getCreationDate());
        response.setIsClosed(announcement.getIsClosed());

        return response;
    }

    private AnnouncementEntity createAndSaveAnnouncement(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcement = new AnnouncementEntity();

        announcement.setUserId(addNewAnnouncementRequest.getUserId());
        announcement.setAnnouncementDetailsEntity(this.createAnnouncementDetailsEntity(addNewAnnouncementRequest));
        announcement.setCreationDate(LocalDate.now());
        announcement.setIsClosed(false);
        this.announcementRepository.save(announcement);

        return announcement;
    }

    private AnnouncementDetailsEntity createAnnouncementDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcementDetails = new AnnouncementDetailsEntity();

        announcementDetails.setAnnouncementContent(this.createAnnouncementContentEntity(addNewAnnouncementRequest));
        announcementDetails.setAddressDetailsEntity(this.createAddressDetailsEntity(addNewAnnouncementRequest));
        announcementDetails.setTitle(addNewAnnouncementRequest.getTitle());
        announcementDetails.setMainPhotoPath(addNewAnnouncementRequest.getMainPhotoPath());
        announcementDetails.setCaution(addNewAnnouncementRequest.getCaution());
        announcementDetails.setRentalAmount(addNewAnnouncementRequest.getRentalAmount());
        announcementDetails.setRoomsNumber(addNewAnnouncementRequest.getRoomsNumber());
        announcementDetails.setRentalTerm(addNewAnnouncementRequest.getRentalTerm());

        return this.announcementDetailsRepository.save(announcementDetails);
    }

    private AnnouncementContentEntity createAnnouncementContentEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var announcementContent = new AnnouncementContentEntity();

        var photoPaths = new ArrayList<PhotoPathEntity>();
        addNewAnnouncementRequest.getPhotoPaths().forEach(pp -> {
            var photoPath = new PhotoPathEntity();
            photoPath.setPhotoPath(pp);
            photoPaths.add(this.photoPathRepository.save(photoPath));
        });
        announcementContent.setContent(addNewAnnouncementRequest.getContent());
        announcementContent.setPhotoPaths(photoPaths);

        return announcementContentRepository.save(announcementContent);
    }

    private AddressDetailsEntity createAddressDetailsEntity(AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var addressDetails = new AddressDetailsEntity();
        addressDetails.setDistrict(addNewAnnouncementRequest.getDistrict());
        addressDetails.setCity(addNewAnnouncementRequest.getCity());
        addressDetails.setZipCode(addNewAnnouncementRequest.getZipCode());
        addressDetails.setStreet(addNewAnnouncementRequest.getStreet());
        addressDetails.setBuildingNumber(addNewAnnouncementRequest.getBuildingNumber());
        addressDetails.setLocalNumber(addNewAnnouncementRequest.getLocalNumber());

        return this.addressDetailsRepository.save(addressDetails);
    }

    @Override
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(Long announcementId) throws AnnouncementNotFoundException {
        var announcement = this.announcementRepository.findById(announcementId);
        if (announcement.isEmpty())
            throw new AnnouncementNotFoundException();

        return this.buildResponse(announcement.get());
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
                .title(announcementDetails.getTitle())
                .mainPhotoPath(announcementDetails.getMainPhotoPath())
                .roomsNumber(announcementDetails.getRoomsNumber())
                .rentalTerm(announcementDetails.getRentalTerm())
                .caution(announcementDetails.getCaution())
                .rentalAmount(announcementDetails.getRentalAmount())
                .city(addressDetails.getCity())
                .street(addressDetails.getStreet())
                .district(addressDetails.getDistrict())
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
                                                         Long requesterId) throws AnnouncementNotFoundException, ClosedAnnouncementException {
        var announcement = this.announcementRepository.findById(announcementId);
        if (announcement.isEmpty() || !isUserIdValid(announcement.get().getUserId(), requesterId))
            throw new AnnouncementNotFoundException();
        if (announcement.get().getIsClosed().equals(true))
            throw new ClosedAnnouncementException();

        this.updateAnnouncementEntity(announcement.get(), updateAnnouncementRequest);

        return this.announcementMapper.updateAnnouncementRequestToUpdateAnnouncementResponse(updateAnnouncementRequest);
    }

    private void updateAnnouncementEntity(AnnouncementEntity announcement,
                                          UpdateAnnouncementRequest updateAnnouncementRequest) {

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
            announcementContent.setPhotoPaths(photoPathEntities);
        }

        this.announcementContentRepository.save(announcementContent);
    }

    private void updateAddressDetailsEntity(AddressDetailsEntity addressDetails,
                                            UpdateAnnouncementRequest updateAnnouncementRequest) {
        var city = updateAnnouncementRequest.getCity();
        if (!Objects.isNull(city))
            addressDetails.setCity(city);
        var street = updateAnnouncementRequest.getStreet();
        if (!Objects.isNull(street))
            addressDetails.setStreet(street);
        var district = updateAnnouncementRequest.getDistrict();
        if (!Objects.isNull(district))
            addressDetails.setDistrict(district);
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
    public void closeAnnouncement(Long announcementId, Long requesterId) throws AnnouncementNotFoundException {
        var announcement = this.announcementRepository.findById(announcementId);
        if (announcement.isEmpty() || !isUserIdValid(announcement.get().getUserId(), requesterId))
            throw new AnnouncementNotFoundException();

        announcement.get().setIsClosed(true);
        this.announcementRepository.save(announcement.get());
    }

    private Boolean isUserIdValid(Long userId, Long requesterId) {
        return userId.equals(requesterId);
    }

}
