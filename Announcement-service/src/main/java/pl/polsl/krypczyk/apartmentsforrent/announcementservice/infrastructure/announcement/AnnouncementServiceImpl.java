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
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.dto.AnnouncementDTO;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.excpetion.AnnouncementNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.request.CreateAnnouncementRequest;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.CreateAnnouncementResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcement.response.GetAnnouncementWithAllDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.AnnouncementContentRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.PhotoPathEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementcontent.PhotoPathRepository;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.announcementservice.domain.announcementdetails.AnnouncementDetailsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
    public CreateAnnouncementResponse createAnnouncement(CreateAnnouncementRequest createAnnouncementRequest) {
        var announcement = new AnnouncementEntity();

        announcement.setUserId(createAnnouncementRequest.getUserId());
        announcement.setAnnouncementDetailsEntity(this.createAnnouncementDetailsEntity(createAnnouncementRequest));
        announcement.setCreationDate(LocalDate.now());
        announcement.setIsClosed(false);
        this.announcementRepository.save(announcement);

        var response = this.announcementMapper.createAnnouncementRequestToCreateAnnouncementResponse(createAnnouncementRequest);
        response.setCreationDate(announcement.getCreationDate());

        return response;
    }

    private AnnouncementDetailsEntity createAnnouncementDetailsEntity(CreateAnnouncementRequest createAnnouncementRequest) {
        var announcementDetails = new AnnouncementDetailsEntity();

        announcementDetails.setAnnouncementContent(this.createAnnouncementContentEntity(createAnnouncementRequest));
        announcementDetails.setAddressDetailsEntity(this.createAddressDetailsEntity(createAnnouncementRequest));
        announcementDetails.setTitle(createAnnouncementRequest.getTitle());
        announcementDetails.setMainPhotoPath(createAnnouncementRequest.getMainPhotoPath());
        announcementDetails.setCaution(createAnnouncementRequest.getCaution());
        announcementDetails.setRentalAmount(createAnnouncementRequest.getRentalAmount());
        announcementDetails.setRoomsNumber(createAnnouncementRequest.getRoomsNumber());
        announcementDetails.setRentalTerm(createAnnouncementRequest.getRentalTerm());

        return this.announcementDetailsRepository.save(announcementDetails);
    }

    private AnnouncementContentEntity createAnnouncementContentEntity(CreateAnnouncementRequest createAnnouncementRequest) {
        var announcementContent = new AnnouncementContentEntity();

        var photoPaths = new ArrayList<PhotoPathEntity>();
        createAnnouncementRequest.getPhotoPaths().forEach(pp -> {
            var photoPath = new PhotoPathEntity();
            photoPath.setPhotoPath(pp);
            photoPaths.add(this.photoPathRepository.save(photoPath));
        });
        announcementContent.setContent(createAnnouncementRequest.getContent());
        announcementContent.setPhotoPaths(photoPaths);

        return announcementContentRepository.save(announcementContent);
    }

    private AddressDetailsEntity createAddressDetailsEntity(CreateAnnouncementRequest createAnnouncementRequest) {
        var addressDetails = new AddressDetailsEntity();
        addressDetails.setDistrict(createAnnouncementRequest.getDistrict());
        addressDetails.setCity(createAnnouncementRequest.getCity());
        addressDetails.setZipCode(createAnnouncementRequest.getZipCode());
        addressDetails.setStreet(createAnnouncementRequest.getStreet());
        addressDetails.setBuildingNumber(createAnnouncementRequest.getBuildingNumber());
        addressDetails.setLocalNumber(createAnnouncementRequest.getLocalNumber());

        return this.addressDetailsRepository.save(addressDetails);
    }

    @Override
    public GetAnnouncementWithAllDetailsResponse getAnnouncementWithAllDetails(Long announcementId) {
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

}
