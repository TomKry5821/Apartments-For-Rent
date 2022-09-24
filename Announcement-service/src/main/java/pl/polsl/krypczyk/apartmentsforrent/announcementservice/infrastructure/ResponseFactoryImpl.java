package pl.polsl.krypczyk.apartmentsforrent.announcementservice.infrastructure;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.AddNewAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.request.UpdateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.AddNewAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.ObserveAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.application.announcement.response.UpdateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.ResponseFactory;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.AnnouncementMapper;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.photopath.PhotoPathEntity;

import java.util.stream.Collectors;

@Component
@Transactional
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

        return announcementDTO;
    }

    @Override
    public AddNewAnnouncementResponse createAddNewAnnouncementResponse(AnnouncementEntity announcement, AddNewAnnouncementRequest addNewAnnouncementRequest) {
        var response = this.announcementMapper.createAnnouncementRequestToCreateAnnouncementResponse(addNewAnnouncementRequest);
        response.setAnnouncementId(announcement.getId());
        response.setCreationDate(announcement.getCreationDate());
        response.setIsClosed(announcement.getIsClosed());

        return response;
    }

    @Override
    public GetAnnouncementWithAllDetailsResponse createGetAnnouncementWithAllDetailsResponse(AnnouncementEntity announcement) {
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
    public UpdateAnnouncementResponse createUpdateAnnouncementResponse(UpdateAnnouncementRequest updateAnnouncementRequest) {
        return this.announcementMapper.updateAnnouncementRequestToUpdateAnnouncementResponse(updateAnnouncementRequest);
    }

    @Override
    public ObserveAnnouncementResponse createObserveAnnouncementResponse(Long userId, Long announcementId) {
        return ObserveAnnouncementResponse
                .builder()
                .userId(userId)
                .announcementId(announcementId)
                .build();
    }
}
