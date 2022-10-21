package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.observedannouncement.dto.ObservedAnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementMapper;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;

import java.util.stream.Collectors;

@Component
@Transactional
@Slf4j
public class ResponseFactoryImpl implements ResponseFactory {

    private final AnnouncementMapper announcementMapper = Mappers.getMapper(AnnouncementMapper.class);

    @Override
    public AnnouncementDTO createAnnouncementDTO(AnnouncementEntity announcement) {
        var announcementDetails = announcement.getAnnouncementDetailsEntity();
        var announcementDTO = announcementMapper.announcementEntityToAnnouncementDTO(announcement);
        var announcementDetailsDTO = announcementMapper.announcementDetailsEntityToAnnouncementDetailsDTO(announcementDetails);
        announcementDTO.setAnnouncementDetailsDTO(announcementDetailsDTO);
        announcementDTO.setDistrict(announcement.getDistrict());
        announcementDTO.setCity(announcementDTO.getCity());

        log.trace("Created create announcement DTO - " + announcementDTO);
        return announcementDTO;
    }

    @Override
    public AddNewAnnouncementResponse createAddNewAnnouncementResponse(AnnouncementEntity announcement, AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var addNewAnnouncementResponse = this.announcementMapper.createAnnouncementRequestToCreateAnnouncementResponse(addNewAnnouncementRequest);
        addNewAnnouncementResponse.setAnnouncementId(announcement.getId());
        addNewAnnouncementResponse.setCreationDate(announcement.getCreationDate());
        addNewAnnouncementResponse.setIsClosed(announcement.getIsClosed());

        log.trace("create add new announcement response - " + addNewAnnouncementResponse);
        return addNewAnnouncementResponse;
    }

    @Override
    public GetAnnouncementWithAllDetailsResponse createGetAnnouncementWithAllDetailsResponse(AnnouncementEntity announcement) {
        var announcementDetails = announcement.getAnnouncementDetailsEntity();
        var addressDetails = announcementDetails.getAddressDetailsEntity();
        var announcementContent = announcementDetails.getAnnouncementContent();
        var photoPaths = announcementContent.getPhotoPaths();

        var getAnnouncementWithAllDetailsResponse = GetAnnouncementWithAllDetailsResponse
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

        log.trace("Created get announcement with all details response - " + getAnnouncementWithAllDetailsResponse);
        return getAnnouncementWithAllDetailsResponse;
    }

    @Override
    public UpdateAnnouncementResponse createUpdateAnnouncementResponse(UpdateAnnouncementRequest updateAnnouncementRequest) {
        var updateAnnouncementResponse = this.announcementMapper.updateAnnouncementRequestToUpdateAnnouncementResponse(updateAnnouncementRequest);

        log.trace("Created update announcement response - " + updateAnnouncementResponse);
        return updateAnnouncementResponse;
    }

    @Override
    public ObserveAnnouncementResponse createObserveAnnouncementResponse(Long userId, Long announcementId) {
        var observeAnnouncementResponse = ObserveAnnouncementResponse
                .builder()
                .userId(userId)
                .announcementId(announcementId)
                .build();

        log.trace("Created observed announcement response -" + observeAnnouncementResponse);
        return observeAnnouncementResponse;
    }

    @Override
    public ObservedAnnouncementDTO createObservedAnnouncementDTO(AnnouncementEntity announcement) {
        var observedAnnouncementDTO = ObservedAnnouncementDTO
                .builder()
                .title(announcement.getAnnouncementDetailsEntity().getTitle())
                .userId(announcement.getUserId())
                .mainPhotoPath(announcement.getAnnouncementDetailsEntity().getMainPhotoPath())
                .build();

        log.trace("Created observed announcement DTO - " + observedAnnouncementDTO);
        return observedAnnouncementDTO;
    }
}
